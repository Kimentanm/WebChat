package com.hsd.core;


import com.hsd.dao.LogAuditMapper;
import com.hsd.model.LogAudit;
import com.hsd.security.SecurityUtils;
import com.hsd.util.JsonMapper;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

import static com.hsd.core.HsdConstant.*;

/**
 * 基于通用MyBatis Mapper插件的Service接口的实现
 */
public abstract class AbstractService<T> implements Service<T> {
    private final Logger log = LoggerFactory.getLogger(AbstractService.class);


    private static String field_name_create_user_id = "createdBy";

    private static String field_name_create_date = "createdDate";

    private static String field_name_update_user_id = "lastModifiedBy";

    private static String field_name_update_date = "lastModifiedDate";

    private static String field_name_version = "version";
    private static String field_name_status = "status";

    @Autowired
    protected Mapper<T> mapper;

    @Autowired
    protected LogAuditMapper logAuditMapper;

    private Class<T> modelClass;    // 当前泛型真实类型的Class

    public AbstractService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    private boolean isFieldExist(T entity, String fieldName) {
        Field obj = Reflections.getAccessibleField(entity, fieldName);
        if (obj != null) {
            return true;
        }
        return false;
    }

    private void populateAuditInfo(T model, String actionType) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        LocalDateTime currentDate = LocalDateTime.now();
        if (isFieldExist(model, field_name_create_date)) {
            LocalDateTime createDate = (LocalDateTime) Reflections.invokeGetter(model, field_name_create_date);
            if (createDate == null) {
                Reflections.invokeSetter(model, field_name_create_date, currentDate);
            }
        }

        if (isFieldExist(model, field_name_create_user_id)) {
            Long createUserId = (Long) Reflections.invokeGetter(model, field_name_create_user_id);
            if (createUserId == null) {
                Reflections.invokeSetter(model, field_name_create_user_id, currentUserId);
            }

        }

        if (isFieldExist(model, field_name_update_date)) {
//            Date udateDate = (Date)Reflections.invokeGetter(model, field_name_update_date);
//            if(udateDate == null){
            Reflections.invokeSetter(model, field_name_update_date, currentDate);
//            }
        }

        if (isFieldExist(model, field_name_update_user_id)) {
//            Long updateUserId = (Long)Reflections.invokeGetter(model, field_name_update_user_id);
//            if(updateUserId == null){
            Reflections.invokeSetter(model, field_name_update_user_id, currentUserId);
//            }

        }

        if (isFieldExist(model, field_name_version)) {
            if ("update".equalsIgnoreCase(actionType)) {
                Integer version = (Integer) Reflections.invokeGetter(model, field_name_version);
                if(version == null){
                    version = 0;
                }
                T currentEntity = mapper.selectByPrimaryKey(model);
                Integer currentVersion = (Integer) Reflections.invokeGetter(currentEntity, field_name_version);

//                if (currentEntity == null || (currentVersion != null && !version.equals(currentVersion)) ) {
//                    throw new RuntimeException(String.format("concurrent update for entity: {0}", model));
//                }

                Reflections.invokeSetter(model, field_name_version, version + 1);
                try {
                    if (model.getClass().isAnnotationPresent(Audit.class)) {
                        LogAudit audit = new LogAudit();
                        audit.setAction(LOG_AUDIT_ACTION_UPDATE);
                        audit.setCreatedDate(currentDate);
                        audit.setLastModifiedDate(currentDate);
                        audit.setCreatedBy(currentUserId);
                        audit.setLastModifiedBy(currentUserId);
                        audit.setVersion(1);
                        audit.setModel(model.getClass().getSimpleName());
                        audit.setBeforeImage(JsonMapper.nonEmptyMapper().toJson(currentEntity));
                        audit.setAfterImage(JsonMapper.nonEmptyMapper().toJson(model));
                        logAuditMapper.insert(audit);
                    }

                } catch (Exception e) {
                    log.error("error occured when audit the model", e);
                }


            } else {
                Reflections.invokeSetter(model, field_name_version, 1);
            }

        }

    }

    private void populateDeleteAuditInfo(Long id) {
        try {
            Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            if (((Class<T>)type).isAnnotationPresent(Audit.class)) {
                Long currentUserId = SecurityUtils.getCurrentUserId();
                LocalDateTime currentDate = LocalDateTime.now();
                T currentEntity = mapper.selectByPrimaryKey(id);

                LogAudit audit = new LogAudit();
                audit.setAction(LOG_AUDIT_ACTION_DELETE);
                audit.setCreatedDate(currentDate);
                audit.setLastModifiedDate(currentDate);
                audit.setCreatedBy(currentUserId);
                audit.setLastModifiedBy(currentUserId);
                audit.setVersion(1);
                audit.setModel(((Class<T>)type).getSimpleName());
                audit.setBeforeImage(JsonMapper.nonEmptyMapper().toJson(currentEntity));
                logAuditMapper.insert(audit);
            }

        } catch (Exception e) {
            log.error("error occured when audit the model", e);
        }
    }


    public void save(T model) {

        this.populateAuditInfo(model, "create");

        mapper.insertSelective(model);
    }

    public void save(List<T> models) {
        models.forEach(model -> {
            this.populateAuditInfo(model, "create");
            Reflections.invokeSetter(model, field_name_version, 1);
        });
        mapper.insertList(models);
    }

    public void deleteById(Long id) {
        this.populateDeleteAuditInfo(id);
        mapper.deleteByPrimaryKey(id);
    }

    public void deleteSoftById(Long id) {
        this.populateDeleteAuditInfo(id);
        T model = mapper.selectByPrimaryKey(id);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        LocalDateTime currentDate = LocalDateTime.now();

        if (isFieldExist(model, field_name_update_date)) {
            Reflections.invokeSetter(model, field_name_update_date, currentDate);
        }

        if (isFieldExist(model, field_name_update_user_id)) {
            Reflections.invokeSetter(model, field_name_update_user_id, currentUserId);

        }
        Reflections.invokeSetter(model, field_name_status, "DELETED");
        mapper.updateByPrimaryKeySelective(model);
    }

//    public void deleteByIds(String ids) {
//        mapper.deleteByIds(ids);
//    }

    public void update(T model) {
        this.populateAuditInfo(model, "update");

        mapper.updateByPrimaryKeySelective(model);
    }

    public T findById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public T findBy(String fieldName, Object value) throws TooManyResultsException {
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return mapper.selectOne(model);
        } catch (ReflectiveOperationException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

//    public List<T> findByIds(String ids) {
//        return mapper.selectByIds(ids);
//    }

    public List<T> findByCondition(Condition condition) {
        return mapper.selectByCondition(condition);
    }

    public List<T> findAll() {
        return mapper.selectAll();
    }
}

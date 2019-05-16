package com.hsd.service.impl;

import com.hsd.core.AbstractService;
import com.hsd.dao.LogAuditMapper;
import com.hsd.model.LogAudit;
import com.hsd.service.LogAuditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/01/06.
 */
@Service
@Transactional
public class LogAuditServiceImpl extends AbstractService<LogAudit> implements LogAuditService {
    @Resource
    private LogAuditMapper thngLogAuditMapper;

}

package com.hsd.service.impl;

import com.hsd.dao.RoleMapper;
import com.hsd.model.Role;
import com.hsd.service.RoleService;
import com.hsd.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/04/16.
 */
@Service
@Transactional
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        return roleMapper.findRolesByUserId(userId);
    }

    @Override
    public Role findByCode(String code) {
        return roleMapper.findByCode(code);
    }
}

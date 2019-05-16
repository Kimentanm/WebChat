package com.hsd.service.impl;

import com.hsd.dao.UserRoleMapper;
import com.hsd.model.UserRole;
import com.hsd.service.UserRoleService;
import com.hsd.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/04/16.
 */
@Service
@Transactional
public class UserRoleServiceImpl extends AbstractService<UserRole> implements UserRoleService {
    @Resource
    private UserRoleMapper tblUserRoleMapper;

}

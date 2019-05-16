package com.hsd.dao;

import com.hsd.core.Mapper;
import com.hsd.model.Role;

import java.util.List;

public interface RoleMapper extends Mapper<Role> {
    List<Role> findRolesByUserId(Long userId);

    Role findByCode(String code);
}
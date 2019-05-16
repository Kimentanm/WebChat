package com.hsd.service;
import com.hsd.model.Role;
import com.hsd.core.Service;

import java.util.List;


/**
 * Created by CodeGenerator on 2018/04/16.
 */
public interface RoleService extends Service<Role> {

    List<Role> findRolesByUserId(Long userId);

    Role findByCode(String code);
}

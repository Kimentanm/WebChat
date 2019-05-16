package com.hsd.service;

import com.hsd.model.User;
import com.hsd.core.Service;

import java.util.List;


/**
 * Created by CodeGenerator on 2018/04/16.
 */
public interface UserService extends Service<User> {

    User getUserIdentity();

    boolean saveUser(User model);

    List<User> findUserByName(String name);

    void updatePassword(User user);

    void updateImageUrl(String imageUrl);
}

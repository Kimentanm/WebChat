package com.hsd.service;

import com.hsd.model.UserFriend;
import com.hsd.core.Service;

import java.util.List;


/**
 * Created by CodeGenerator on 2018/04/17.
 */
public interface UserFriendService extends Service<UserFriend> {
    List<Long> findByUserId(Long userId);

    boolean addFriend(Long friendId);

    void deleteFriend(Long id);
}

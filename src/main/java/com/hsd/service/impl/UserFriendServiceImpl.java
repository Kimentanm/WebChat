package com.hsd.service.impl;

import com.hsd.dao.UserFriendMapper;
import com.hsd.model.User;
import com.hsd.model.UserFriend;
import com.hsd.security.SecurityUtils;
import com.hsd.service.UserFriendService;
import com.hsd.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/04/17.
 */
@Service
@Transactional
public class UserFriendServiceImpl extends AbstractService<UserFriend> implements UserFriendService {
    @Resource
    private UserFriendMapper tblUserFriendMapper;

    /**
     * 查找出当前用户的朋友的id
     * @param userId
     * @return
     */
    @Override
    public List<Long> findByUserId(Long userId) {
        List<Long> list = new ArrayList<>();
        List<UserFriend> friendsByUserId = tblUserFriendMapper.findByUserId(userId);
        List<UserFriend> friendsByFriendId = tblUserFriendMapper.findByFriendId(userId);
        if (friendsByUserId != null) {
            for (UserFriend userFriend : friendsByUserId) {
                list.add(userFriend.getFriendId());
            }
        }
        if (friendsByFriendId != null) {
            for (UserFriend userFriend : friendsByFriendId) {
                list.add(userFriend.getUserId());
            }
        }
        return list;
    }

    @Override
    public boolean addFriend(Long friendId) {
        List<Long> list = findByUserId(SecurityUtils.getCurrentUserId());
        boolean flag = true;
        for (Long id : list) {
            if (friendId.equals(id)) {
                flag = false;
            }
        }
        if (flag) {
            UserFriend userFriend = new UserFriend();
            userFriend.setFriendId(friendId);
            userFriend.setUserId(SecurityUtils.getCurrentUserId());
            this.save(userFriend);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteFriend(Long friendId) {
        Long userId = SecurityUtils.getCurrentUserId();
        tblUserFriendMapper.deleteFriend(userId,friendId);
    }
}

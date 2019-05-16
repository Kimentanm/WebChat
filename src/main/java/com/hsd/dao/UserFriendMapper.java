package com.hsd.dao;

import com.hsd.core.Mapper;
import com.hsd.model.UserFriend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFriendMapper extends Mapper<UserFriend> {
    List<UserFriend> findByUserId(Long userId);

    List<UserFriend> findByFriendId(Long userId);

    void deleteFriend(@Param("userId") Long userId, @Param("friendId")Long friendId);
}
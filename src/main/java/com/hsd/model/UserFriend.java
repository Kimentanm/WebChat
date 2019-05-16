package com.hsd.model;

import javax.persistence.*;

@Table(name = "tbl_user_friend")
public class UserFriend {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "friend_id")
    private Long friendId;

    /**
     * @return user_id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return friend_id
     */
    public Long getFriendId() {
        return friendId;
    }

    /**
     * @param friendId
     */
    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
}
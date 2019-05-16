package com.hsd.service.impl;

import com.hsd.core.HsdConstant;
import com.hsd.core.Result;
import com.hsd.dao.UserMapper;
import com.hsd.model.*;
import com.hsd.security.SecurityUtils;
import com.hsd.service.*;
import com.hsd.core.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/04/16.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleService roleService;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private UserFriendService userFriendService;
    @Resource
    private ChatHistoryService chatHistoryService;
    @Resource
    private MessageService messageService;

    @Override
    public User getUserIdentity() {
        User user = userMapper.findById(SecurityUtils.getCurrentUserId());
        if (user != null) {
            //添加角色
            user.setRoles(roleService.findRolesByUserId(user.getId()));
            //添加好友列表
            List<Long> friendIdList = userFriendService.findByUserId(user.getId());
            List<User> friends = new ArrayList<>();
            for (Long friendId : friendIdList) {
                //计算未读消息
                List<ChatHistory> chatHistoryList = chatHistoryService.findFriendChatHistory(friendId);
                int count = 0;
                if (chatHistoryList.size() != 0) {
                    for (ChatHistory chatHistory : chatHistoryList) {
                        if (!chatHistory.getReadMessage()) {
                            count++;
                        }
                    }
                }
                User friend = userMapper.findById(friendId);
                friend.setCount(count);
                friends.add(friend);
            }
            user.setFriends(friends);

            List<Message> list = messageService.findByReceiverWithoutRead(user.getId());
            boolean flag = false;
            for (Message message : list) {
                if (message.getStatus().equals(HsdConstant.MESSAGE_UNREAD)) {
                    flag = true;
                }
            }
            user.setMessageUnread(flag);
        }
        return user;
    }

    @Override
    public boolean saveUser(User user) {
        User userDB = userMapper.findByLoginName(user.getLoginName());
        //如果用户名重复返回错误
        if (userDB == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setImageUrl("http://img.kimen.xyz/psb.png");
            super.save(user);
            Role role = roleService.findByCode(HsdConstant.USER_ROLE_TYPE);
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRoleService.save(userRole);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<User> findUserByName(String name) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<User> users = userMapper.findByName(name);
        users.removeIf(user -> user.getId().equals(currentUserId));
        List<Long> userFriendId = userFriendService.findByUserId(currentUserId);
        users.stream().forEach(user -> {
            boolean flag = false;
            for (Long friendId : userFriendId) {
                if (user.getId().equals(friendId)) {
                    flag = true;
                }
            }
            user.setFriend(flag);
        });
        return users;
    }

    @Override
    public void updatePassword(User user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            super.update(user);
        }
    }

    @Override
    public void updateImageUrl(String imageUrl) {
        userMapper.updateImageUrl(SecurityUtils.getCurrentUserId(),imageUrl);
    }
}

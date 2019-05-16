package com.hsd.service.impl;

import com.hsd.dao.ChatHistoryMapper;
import com.hsd.model.ChatHistory;
import com.hsd.service.ChatHistoryService;
import com.hsd.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hsd.security.SecurityUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/04/18.
 */
@Service
@Transactional
public class ChatHistoryServiceImpl extends AbstractService<ChatHistory> implements ChatHistoryService {
    @Resource
    private ChatHistoryMapper chatHistoryMapper;

    @Override
    public List<ChatHistory> findCurrentUserChatHistory(Long sender, Long receiver) {
        List<ChatHistory> list = chatHistoryMapper.findCurrentUserChatHistory(sender,receiver);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        list.stream().forEach(item -> {
            if (item.getSender().equals(currentUserId)) {
                item.setSelf(true);
            } else {
                item.setReadMessage(true);
                item.setSelf(false);
                update(item);
            }
        });
        Collections.sort(list, new Comparator<ChatHistory>() {
            @Override
            public int compare(ChatHistory o1, ChatHistory o2) {
                try{
                    if (o1.getTime().isBefore(o2.getTime())){
                        return -1;
                    }else if(o1.getTime().isAfter(o2.getTime())){
                        return 1;
                    }else {
                        return 0;
                    }
                }catch (Exception e){

                }
                return 0;
            }
        });
        return list;
    }

    @Override
    public List<ChatHistory> findFriendChatHistory(Long friendId) {
        return chatHistoryMapper.findFriendChatHistory(friendId,SecurityUtils.getCurrentUserId());
    }

    @Override
    public void readHistory(Long sendId) {
        chatHistoryMapper.readHistory(sendId);
    }
}

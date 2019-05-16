package com.hsd.service;
import com.hsd.model.ChatHistory;
import com.hsd.core.Service;

import java.util.List;


/**
 * Created by CodeGenerator on 2018/04/18.
 */
public interface ChatHistoryService extends Service<ChatHistory> {

    List<ChatHistory> findCurrentUserChatHistory(Long sender, Long receiver);

    List<ChatHistory> findFriendChatHistory(Long friendId);

    void readHistory(Long sendId);
}

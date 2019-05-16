package com.hsd.dao;

import com.hsd.core.Mapper;
import com.hsd.model.ChatHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChatHistoryMapper extends Mapper<ChatHistory> {
    List<ChatHistory> findCurrentUserChatHistory(@Param("sender") Long sender, @Param("receiver")Long receiver);

    List<ChatHistory> findFriendChatHistory(@Param("sender") Long sender, @Param("receiver")Long receiver);

    void readHistory(Long sendId);
}
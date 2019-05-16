package com.hsd.dao;

import com.hsd.core.Mapper;
import com.hsd.model.Message;

import java.util.List;

public interface MessageMapper extends Mapper<Message> {
    List<Message> findByReceiver(Long receiver);
}
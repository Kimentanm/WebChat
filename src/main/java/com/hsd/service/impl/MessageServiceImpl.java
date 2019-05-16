package com.hsd.service.impl;

import com.hsd.core.HsdConstant;
import com.hsd.dao.MessageMapper;
import com.hsd.model.Message;
import com.hsd.service.MessageService;
import com.hsd.core.AbstractService;
import com.hsd.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/04/28.
 */
@Service
@Transactional
public class MessageServiceImpl extends AbstractService<Message> implements MessageService {
    @Resource
    private MessageMapper hsdMessageMapper;
    @Lazy
    @Resource
    private UserService userService;

    @Override
    public void save(Message model) {
        model.setStatus(HsdConstant.MESSAGE_UNREAD);
        model.setTime(LocalDateTime.now());
        super.save(model);
    }

    @Override
    public List<Message> findByReceiver(Long receiver) {
        List<Message> list = hsdMessageMapper.findByReceiver(receiver);
        list.stream().forEach(item -> {
            item.setSenderName(userService.findById(item.getSender()).getName());
            item.setStatus(HsdConstant.MESSAGE_READ);
            update(item);
        });
        return list;
    }

    @Override
    public List<Message> findByReceiverWithoutRead(Long id) {
        return hsdMessageMapper.findByReceiver(id);
    }
}

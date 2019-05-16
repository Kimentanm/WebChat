package com.hsd.ws;

import com.alibaba.fastjson.JSON;
import com.hsd.bo.MessageBO;
import com.hsd.model.User;
import com.hsd.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserService userService;

    @MessageMapping("/chat")
    public void handleChat(MessageBO messageBO) {
        messageBO.setSelf(false);
        messageBO.setFlag("chat");
        logger.info(JSON.toJSONString(messageBO));
        messagingTemplate.convertAndSend("/topic/" + messageBO.getReceiver(), messageBO);
    }

    @MessageMapping("/addFriend")
    public void handleAddFriend(MessageBO messageBO) {
        messageBO.setFlag("addFriend");
        User user = userService.findById(messageBO.getSender());
        messageBO.setUser(user);
        messagingTemplate.convertAndSend("/topic/" + messageBO.getReceiver(), messageBO);
    }

    @MessageMapping("/sendMessage")
    public void handleSendMessage(MessageBO messageBO) {
        messageBO.setFlag("sendMessage");
        messagingTemplate.convertAndSend("/topic/" + messageBO.getReceiver(), messageBO);
    }
}

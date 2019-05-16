package com.hsd.service;
import com.hsd.model.Message;
import com.hsd.core.Service;

import java.util.List;


/**
 * Created by CodeGenerator on 2018/04/28.
 */
public interface MessageService extends Service<Message> {

    List<Message> findByReceiver(Long receiver);

    List<Message> findByReceiverWithoutRead(Long id);
}

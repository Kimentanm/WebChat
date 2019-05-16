package com.hsd.web;

import com.hsd.core.Result;
import com.hsd.core.ResultGenerator;
import com.hsd.model.Message;
import com.hsd.service.MessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/04/28.
*/
@RestController
@RequestMapping("/message")
public class MessageController {
    @Resource
    private MessageService messageService;

    @PostMapping
    public Result add(@RequestBody Message message) {
        //存入hsd_message表中
        messageService.save(message);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        messageService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    public Result update(@RequestBody Message message) {
        messageService.update(message);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Message message = messageService.findById(id);
        return ResultGenerator.genSuccessResult(message);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Message> list = messageService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/receiver/{receiver}")
    public Result findByReceiver(@PathVariable Long receiver) {
        List<Message> list = messageService.findByReceiver(receiver);
        return ResultGenerator.genSuccessResult(list);
    }
}

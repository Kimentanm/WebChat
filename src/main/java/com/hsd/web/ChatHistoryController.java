package com.hsd.web;

import com.alibaba.fastjson.JSON;
import com.hsd.core.Result;
import com.hsd.core.ResultGenerator;
import com.hsd.model.ChatHistory;
import com.hsd.service.ChatHistoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/04/18.
*/
@RestController
@RequestMapping("/chat/history")
public class ChatHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(ChatHistoryController.class);

    @Resource
    private ChatHistoryService chatHistoryService;

    @PostMapping("add")
    public Result add(@RequestBody ChatHistory chatHistory) {
        chatHistory.setReadMessage(false);
        chatHistoryService.save(chatHistory);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        chatHistoryService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    public Result update(@RequestBody ChatHistory chatHistory) {
        chatHistoryService.update(chatHistory);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        ChatHistory chatHistory = chatHistoryService.findById(id);
        return ResultGenerator.genSuccessResult(chatHistory);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ChatHistory> list = chatHistoryService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/current")
    public Result getCurrentUserChatHistory(@RequestParam Long sender, @RequestParam Long receiver) {
        List<ChatHistory> list = chatHistoryService.findCurrentUserChatHistory(sender,receiver);
        return ResultGenerator.genSuccessResult(list);
    }

    @GetMapping("/read/{sendId}")
    public Result readHistory(@PathVariable Long sendId) {
        chatHistoryService.readHistory(sendId);
        return ResultGenerator.genSuccessResult();
    }
}

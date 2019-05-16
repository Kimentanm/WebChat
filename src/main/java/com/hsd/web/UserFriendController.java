package com.hsd.web;

import com.hsd.core.Result;
import com.hsd.core.ResultGenerator;
import com.hsd.model.UserFriend;
import com.hsd.service.UserFriendService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/04/17.
*/
@RestController
@RequestMapping("/user/friend")
public class UserFriendController {
    @Resource
    private UserFriendService userFriendService;

    @PostMapping
    public Result add(@RequestBody UserFriend userFriend) {
        userFriendService.save(userFriend);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        userFriendService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    public Result update(@RequestBody UserFriend userFriend) {
        userFriendService.update(userFriend);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        UserFriend userFriend = userFriendService.findById(id);
        return ResultGenerator.genSuccessResult(userFriend);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<UserFriend> list = userFriendService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/addFriend/{friendId}")
    public Result addFriend(@PathVariable String friendId) {
        boolean reslut = userFriendService.addFriend(Long.valueOf(friendId));
        if (reslut) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("添加失败，当前用户已是您的好友！");
        }
    }

    @GetMapping("/delete/{id}")
    public Result deleteFriend(@PathVariable String id) {
        userFriendService.deleteFriend(Long.valueOf(id));
        return ResultGenerator.genSuccessResult();
    }
}

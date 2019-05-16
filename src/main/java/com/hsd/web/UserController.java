package com.hsd.web;

import com.hsd.core.Result;
import com.hsd.core.ResultGenerator;
import com.hsd.model.User;
import com.hsd.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hsd.util.QueryUtil;
import com.hsd.util.qiniuyun.FileResultDTO;
import com.hsd.util.qiniuyun.QiniuyunServiceManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
* Created by CodeGenerator on 2018/04/16.
*/
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private QiniuyunServiceManager qiniuyunManager;

    @Resource
    private UserService userService;

    @PostMapping
    public Result add(@RequestBody User user) {
        boolean repetition = userService.saveUser(user);
        if (repetition) {
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("登录名已存在，请重新输入用户名！");
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    public Result update(@RequestBody User user) {
        userService.update(user);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping("/password")
    public Result updatePassword(@RequestBody User user) {
        userService.updatePassword(user);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResultGenerator.genSuccessResult(user);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<User> list = userService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/identity")
    public Result getUserIdentity(){
        logger.info("用户登陆啦！！");
        User user = userService.getUserIdentity();
        return ResultGenerator.genSuccessResult(user);
    }

    @GetMapping("/search")
    public Result getUserByName(@RequestParam String name){
        name = QueryUtil.replaceForLike(name);
        List<User> users = new ArrayList<>();
        if (StringUtils.isNotEmpty(name)) {
            users.addAll(userService.findUserByName(name));
        }
        return ResultGenerator.genSuccessResult(users);
    }

    @PostMapping("/file/upload")
    public Result uploadFile(HttpServletRequest request, HttpServletResponse response){
        try {
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = mRequest.getFileNames();
            while (iter.hasNext()) {
                FileResultDTO result = qiniuyunManager.uploadInputStream(mRequest.getFile(iter.next()).getBytes());
                userService.updateImageUrl(result.getLocation());
                return ResultGenerator.genSuccessResult(result);
            }
            return ResultGenerator.genFailResult(">>>file upload failed");
        } catch (Exception e) {
            logger.error(">>> file upload faile", e);
            return ResultGenerator.genFailResult(">>>file upload failed");
        }
    }
}

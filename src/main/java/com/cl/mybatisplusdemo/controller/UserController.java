package com.cl.mybatisplusdemo.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cl.mybatisplusdemo.model.User;
import com.cl.mybatisplusdemo.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("user")
    public String saveUser(@RequestBody User user) {
        log.info(user);
        userService.save(user);
        return "插入完毕";
    }

    @PostMapping("getUser")
    public User getUser(@RequestBody String mobile) {
        log.info(mobile);
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getMobile, mobile));
        return user;
    }
}

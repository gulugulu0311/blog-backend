package com.blog.controller;

import com.blog.services.SysUserService;
import com.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private SysUserService userService;
    @GetMapping("currentUser")
    public Result current_user(@RequestHeader("Authorization") String token){
        return userService.getUserInfoByToken(token);
    }
}

package com.lebron.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lebron.springboot.model.User;
import com.lebron.springboot.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public void add(User user) {
        userService.addUser(user);
    }

    @PostMapping("/del")
    public void del(int id) {
        userService.delUser(id);
    }

    @GetMapping
    public List<User> list() {
        return userService.listUser();
    }

    @GetMapping("/page")
    public List<User> page() {
        return userService.pageUser();
    }

    @GetMapping("/select")
    public List<User> select() {
        return userService.select();
    }

    @PostMapping("/update")
    public void update(User user) {
        userService.updateUser(user);
    }
}

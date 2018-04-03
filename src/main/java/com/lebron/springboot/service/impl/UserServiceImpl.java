package com.lebron.springboot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lebron.springboot.mapper.UserMapper;
import com.lebron.springboot.model.User;
import com.lebron.springboot.service.UserService;

@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Override
    public void addUser(User user) {
        super.insert(user);
    }

    @Override
    public void delUser(int id) {
        super.deleteLogicById(id);
    }

    @Override
    public List<User> listUser() {
        return super.selectAll();
    }

    @Override
    public List<User> pageUser() {
        User user = new User();
        user.setName("1");
        super.selectPageListByExample(new User(), 1, 1);
        return null;
    }
    
    @Override
    public void updateUser(User user) {
        super.updateSelective(user);
    }

    @Override
    public List<User> select() {
        return userMapper.selectCurrent();
    }

}

package com.lebron.springboot.mapper;

import java.util.List;

import com.github.abel533.mapper.Mapper;
import com.lebron.springboot.model.User;

public interface UserMapper extends Mapper<User> {

    List<User> selectCurrent();
}

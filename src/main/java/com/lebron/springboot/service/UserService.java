package com.lebron.springboot.service;

import java.util.List;

import com.lebron.springboot.model.User;

public interface UserService {

	void addUser(User user);

	void delUser(int id);

	List<User> listUser();

	List<User> pageUser();

	void updateUser(User user);

	List<User> select();


}

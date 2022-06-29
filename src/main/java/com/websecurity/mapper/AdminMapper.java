package com.websecurity.mapper;


import com.websecurity.pojo.User;

import java.util.List;

public interface AdminMapper {

    List<User> getUserByUsername(User user);

    int insertUser(User user);

    List<User> getUserById(Integer id);

    List<User> getAllUser();
}

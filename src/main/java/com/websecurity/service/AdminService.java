package com.websecurity.service;


import com.websecurity.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AdminService {

    boolean login(User user);

    void registe(User user);

    List<User> queryUserById(Integer id);
}

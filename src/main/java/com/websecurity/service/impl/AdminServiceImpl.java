package com.websecurity.service.impl;


import com.websecurity.mapper.AdminMapper;
import com.websecurity.pojo.User;
import com.websecurity.service.AdminService;
import com.websecurity.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;


    @Override
    public boolean login(User user) {
        List<User> userList = adminMapper.getUserByUsername(user);
        if(userList.size() != 1){
            System.out.println("用户名重复 登陆失败");
            return false;
        }
        for(User u : userList){
            if(u.getPassWord().equals(MD5Util.getMD5(user.getPassWord()))){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    @Override
    public void registe(User user) {
        String username = user.getUserName();
        String MD5PassWord = MD5Util.getMD5(user.getPassWord());
        adminMapper.insertUser(new User(null, username, MD5PassWord));
    }

    @Override
    public List<User> queryUserById(Integer id) {
        return adminMapper.getUserById(id);
    }
}
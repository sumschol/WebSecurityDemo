package com.websecurity.controller;

import com.websecurity.annotation.NeedToken;
import com.websecurity.pojo.User;
import com.websecurity.service.AdminService;
import com.websecurity.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/sql")
public class SQLController {

    private static final String SQL_REG_EXP = ".*(\\b(select|insert" +
            "|into|update|delete|from|where|and|or|trancate" +
            "|drop|execute|like|grant|use|union|order|by)\\b).*";

    @Autowired
    QueueService queueService;

    @Autowired
    AdminService adminService;

    @NeedToken
    @RequestMapping("/sqldemo")
    public ModelAndView sql(HttpServletRequest req){
        List<User> userList = queueService.queueUser(User.class, "select id, user_name userName from user where id = " + req.getParameter("sqlid"));
        for (User u : userList){
            ModelAndView mav = new ModelAndView("main");
            mav.addObject("user", u);
            return mav;
        }
        return new ModelAndView();
    }

    @RequestMapping("/sqlsec")
    public ModelAndView sqlsec(HttpServletRequest req){
        String sqlid = req.getParameter("sqlsecid");
        if(sqlid.toLowerCase().matches(SQL_REG_EXP)){
            ModelAndView mav = new ModelAndView("main");
            return mav.addObject("user2", new User(null, "you are hacker!", ""));
        }
        List<User> userList = adminService.queryUserById(Integer.parseInt(sqlid));
        Iterator<User> iterator = userList.iterator();
        User user2 = iterator.next();
        ModelAndView mav = new ModelAndView("main");
        return mav.addObject("user2", user2);
    }
}
package com.websecurity.controller;

import com.websecurity.annotation.NeedToken;
import com.websecurity.pojo.User;
import com.websecurity.service.AdminService;
import com.websecurity.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AdminController {

    @Autowired
    AdminService adminService;

    @RequestMapping("/")
    public String login(){
        return "login";
    }

    @RequestMapping("/login")
    public ModelAndView checkLogin(HttpServletRequest req, HttpServletResponse resp){
        String username = req.getParameter("name");
        String password = req.getParameter("pwd");
        User user = new User(null, username, password);
        boolean flag = adminService.login(user);
        if(flag){
            req.getSession().setAttribute("admin", user);
            req.getSession().setAttribute("token", MD5Util.getMD5(username));
            Cookie cookie = new Cookie("token", MD5Util.getMD5(username));
            cookie.setMaxAge(60*60*24*10);
            resp.addCookie(cookie);
//            ModelAndView mav = new ModelAndView("main");
//            mav.addObject("admin", user);
            return new ModelAndView("main");
        }else{
            ModelAndView mav = new ModelAndView("login");
            return mav.addObject("loginFailMsg", "用户名或密码错误");
        }
    }

    @RequestMapping("/registerpage")
    public String registerpage(){
        return "register";
    }

    @RequestMapping("/register")
    public String reg(HttpServletRequest req){
        User user = new User(null, req.getParameter("aname"), req.getParameter("apwd"));
        adminService.registe(user);
        return "login";
    }
}
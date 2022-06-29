package com.websecurity.controller;



import com.websecurity.mapper.AdminMapper;
import com.websecurity.pojo.User;
import com.websecurity.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import sun.security.provider.MD5;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Controller
public class TokenController {

    @Autowired
    AdminMapper adminMapper;

    @RequestMapping("/token")
    public ModelAndView getToken(HttpServletRequest req){
        ModelAndView mav = new ModelAndView("main");
        String referer = req.getHeader("Referer");
        if (!req.getHeader("Referer").equals("http://localhost:8080/hacker")) {
            Cookie[] cookies = req.getCookies();
            String token = null;
            for (Cookie c : cookies){
                if (Objects.equals(c.getName(), "token")) {
                    token = c.getValue();
                    break;
                }
            }
            String doLikeUserName = "";
            if (req.getSession().getAttribute("token").equals(token)) {
                List<User> users = adminMapper.getAllUser();
                Iterator<User> iterator = users.iterator();
                while (iterator.hasNext()) {
                    User user = iterator.next();
                    if(MD5Util.getMD5(user.getUserName()).equals(token)){
                        doLikeUserName = user.getUserName();
                        break;
                    }
                }
            }
            return mav.addObject("doLikeUserName", doLikeUserName);
        }else {
            return mav.addObject("doLikeUserName", "非法访问源！");
        }
    }
}

package com.websecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class XSSController {

    @RequestMapping("/xssdemo")
    public ModelAndView xss(HttpServletRequest req){
        String xssText = req.getParameter("xsstext");
        ModelAndView modelAndView = new ModelAndView("main");
        modelAndView.addObject("comment", xssText);
        return modelAndView;
    }
}

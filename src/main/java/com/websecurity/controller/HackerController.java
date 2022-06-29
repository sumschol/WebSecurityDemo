package com.websecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HackerController {

    @RequestMapping("/hacker")
    public String hacker(){
        return "csrf";
    }
}

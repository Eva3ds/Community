package com.nowcoder.community.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/Alpha")
public class AlphaController {

    @RequestMapping("/Hello")
    @ResponseBody
    public String sayHello() {
        return "Hello Spring Boot";
    }

}

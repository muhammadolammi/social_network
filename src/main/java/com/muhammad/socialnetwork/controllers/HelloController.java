package com.muhammad.socialnetwork.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @GetMapping("/")
    public String greet(HttpServletRequest request){
        
    return "Hello "+request.getRemoteUser() + " " + "Your session id is: "+ request.getSession().getId();
    }

}

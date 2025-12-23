package com.muhammad.socialnetwork.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.muhammad.socialnetwork.model.LoginRequest;
import com.muhammad.socialnetwork.model.User;
import com.muhammad.socialnetwork.services.UserService;

@RestController
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder= bCryptPasswordEncoder;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){
        // try {
        //     User createdUser = userService.register(user);
        //     return ResponseEntity.ok("User Registered");
            
        // } catch (Exception e) {
        //     // TODO: handle exception
        //         return ResponseEntity.internalServerError("");

        // }
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            User _ = userService.register(user);
            return ResponseEntity.ok("user registered");    

    }

     @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest body){

        return ResponseEntity.ok(userService.verify(body));
    }


}

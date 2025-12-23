package com.muhammad.socialnetwork.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.muhammad.socialnetwork.model.LoginRequest;
import com.muhammad.socialnetwork.model.User;
import com.muhammad.socialnetwork.repo.UserRepo;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;


    public UserService(UserRepo userRepo, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepo = userRepo;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
    }


    public User register(User user) {
        return  userRepo.save(user);
    }


    public String verify(LoginRequest body) {
        // System.out.println(body);
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(body.username(), body.password()));
        if (authentication.isAuthenticated()){
           return jwtService.generateToken(body.username());
        }
        // System.out.println("failure");
        return "failure";
        
       
    }

}

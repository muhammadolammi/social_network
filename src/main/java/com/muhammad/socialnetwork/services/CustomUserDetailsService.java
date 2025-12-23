package com.muhammad.socialnetwork.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.muhammad.socialnetwork.model.CustomUserDetails;
import com.muhammad.socialnetwork.model.User;
import com.muhammad.socialnetwork.repo.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {
   
    private final UserRepo  userRepo ;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if( user==null){
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("user not found");

        }
        return new CustomUserDetails(user);
    }

}

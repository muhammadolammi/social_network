package com.muhammad.socialnetwork.model;

import java.util.Collection;
import java.util.Collections;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails  implements UserDetails{
    
   private  User user;
    public CustomUserDetails(User user) {
    this.user = user;
}


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Add right roles
       return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public @Nullable String getPassword() {
        // TODO Auto-generated method stub
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return user.getUsername();
    }

}

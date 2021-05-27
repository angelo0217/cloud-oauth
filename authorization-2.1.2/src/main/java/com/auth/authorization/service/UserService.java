package com.auth.authorization.service;

import com.auth.authorization.model.UserDetailVo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {


    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return new UserDetailVo("admin", "123456");
    }
}

package com.auth.authorization.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * Created on 2018-12-22
 *
 * @author dean
 * @email loveangelo0217@gmail.com
 * @since 1.0
 */
public class UserDetailVo implements UserDetails {

    private String account;
    private String pwd;

    public UserDetailVo(String account, String pwd){
        this.account = account;
        this.pwd = pwd;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new Vector<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("admin"));

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.pwd;
    }

    @Override
    public String getUsername() {
        return this.account;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

package org.example.humans.domain.security.userDetails;

import org.example.humans.domain.security.entity.AuthUser;
import org.example.humans.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails extends AuthUser implements UserDetails {
    public CustomUserDetails(User user){
        super(user.getId(), user.getEmail(), user.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.emptyList();
    }

    @Override
    public String getPassword(){
        return super.getPassword();
    }

    @Override
    public String getUsername(){
        return super.getEmail();
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }
}

package com.naturemobility.seoul.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class CustomUserDetails implements UserDetails {
    private String userEmail;
    private String password;
    private Boolean isAccountNonExpired;
    private Boolean isEnabled;
    private List<GrantedAuthority> auth;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return auth;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    //계정이 잠겼는가
    //사용하지 않기 때문에 항상 true
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호가 만료되었는가
    //사용하지 않기 때문에 항상 true
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}

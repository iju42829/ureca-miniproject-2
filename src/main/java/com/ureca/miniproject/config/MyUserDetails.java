package com.ureca.miniproject.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;


@Builder
@AllArgsConstructor
public class MyUserDetails implements UserDetails{

	private static final long serialVersionUID = 1L;
	private final String username;
    private final String password;
    private final String email; // extra field
    private final Collection<? extends GrantedAuthority> authorities;
    public String getEmail() {
        return email;
    }
    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override public boolean isAccountNonExpired() { return true; } //나중에 디테일한 설정을 위한 메소드, 일단은 모두 true
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

}

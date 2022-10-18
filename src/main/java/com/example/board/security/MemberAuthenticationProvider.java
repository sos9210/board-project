package com.example.board.security;

import com.example.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class MemberAuthenticationProvider implements AuthenticationProvider {

    private final MemberService memberService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        User user = (User) memberService.loadUserByUsername(username);
        if(!matchPassword(password, user.getPassword())) {
            throw new BadCredentialsException(username);
        }

        if(!user.isEnabled()) {
            throw new BadCredentialsException(username);
        }
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }
    private boolean matchPassword(String loginPwd, String password) {
        return memberService.passwordEncoder().matches(loginPwd,password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

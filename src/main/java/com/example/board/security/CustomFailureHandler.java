package com.example.board.security;

import com.example.board.util.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final PropertiesConfig properties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "로그인에 실패했습니다.";
        String errorCode = "0";
        if (exception instanceof UsernameNotFoundException) {
            errorCode = "1";
            errorMessage = properties.getProperty("fail.login.id");
        }else if(exception instanceof BadCredentialsException) {
            errorCode = "2";
            errorMessage = properties.getProperty("fail.login.password");
        }else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorCode = "3";
            errorMessage = properties.getProperty("fail.login.auth");
        }
        super.setDefaultFailureUrl("/board/user/login?error="+errorCode+"&errorMessage="+errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}

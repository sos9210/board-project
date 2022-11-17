package com.example.board.security;

import com.example.board.enums.MemberAuthLevelEnum;
import com.example.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@EnableWebSecurity
@Configuration @RequiredArgsConstructor
public class BoardSecurityConfig {

    private final MemberService memberService;
    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationFailureHandler failureHandler;
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(memberService).passwordEncoder(memberService.passwordEncoder());
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        return authenticationManager;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .formLogin().loginPage("/board/user/login")
                .usernameParameter("memberId")
                .passwordParameter("password")
                .loginProcessingUrl("/board/user/login")
                .defaultSuccessUrl("/board/user/forums")
                .failureHandler(failureHandler)
                .and()
                .logout()
                .logoutUrl("/board/user/logout")
                .logoutSuccessUrl("/board/user/login")
                .and()
                .authorizeRequests()
                .antMatchers("/board/admin/**").hasRole(MemberAuthLevelEnum.ADMIN.name())
                .antMatchers("/board/user/forum/write","/board/user/forum/delete/*",
                                        "/board/user/forum/edit/*","/board/user/member-edit")
                .hasAnyRole(MemberAuthLevelEnum.USER.name(),MemberAuthLevelEnum.ADMIN.name())
                .anyRequest().permitAll();
        return http.build();
    }

}

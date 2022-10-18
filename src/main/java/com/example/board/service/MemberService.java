package com.example.board.service;

import com.example.board.domain.Member;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface MemberService  extends UserDetailsService {
    String memberJoin(Member member);
    PasswordEncoder passwordEncoder();
}

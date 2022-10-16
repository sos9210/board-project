package com.example.board.service;

import com.example.board.domain.Member;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {
    String memberJoin(Member member);
}

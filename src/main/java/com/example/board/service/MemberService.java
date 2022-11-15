package com.example.board.service;

import com.example.board.domain.Member;
import com.example.board.dto.MemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface MemberService  extends UserDetailsService {
    String joinMember(Member member);
    PasswordEncoder passwordEncoder();
    Member findMember(String memberId);
    Page<MemberDTO> listMember(MemberDTO dto, Pageable pageable);
}

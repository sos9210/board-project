package com.example.board.service.impl;

import com.example.board.domain.Member;
import com.example.board.repository.MemberRepository;
import com.example.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    @Override
    public String memberJoin(Member member) {
        Optional<Member> findByMember = memberRepository.findById(member.getMemberId());
        if(!findByMember.isEmpty()){
            member.encodePassword(member.getPassword());
            throw new IllegalArgumentException("중복ID가 존재합니다.");
        }
        return memberRepository.save(member).getMemberId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findByMember = memberRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(findByMember.getMemberId(),findByMember.getPassword(),authorities(findByMember.getAuthLevel()));
    }

    private Collection<? extends GrantedAuthority> authorities(String authLevel) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + authLevel));
    }
}

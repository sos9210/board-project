package com.example.board.service.impl;

import com.example.board.domain.Member;
import com.example.board.dto.MemberDTO;
import com.example.board.repository.MemberRepository;
import com.example.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    @Override
    public String joinMember(Member member) {
        Optional<Member> findByMember = memberRepository.findById(member.getMemberId());
        if(!findByMember.isEmpty()){
            throw new IllegalArgumentException("중복ID가 존재합니다.");
        }
        member.encodePassword(passwordEncoder().encode(member.getPassword()));
        return memberRepository.save(member).getMemberId();
    }

    public Member findMember(String memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        return findMember.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public Page<MemberDTO> findMembers(Pageable pageable) {
        return memberRepository.findMembers(pageable);
    }

    @Override
    public Member editMember(MemberDTO dto) {

        Member findMember = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        dto.setPassword(passwordEncoder().encode(dto.getPassword()));
        findMember.memberEdit(dto);

        return findMember;
    }

    @Override
    public void memberSecession(String memberId) {
        Member findByMember = memberRepository.findById(memberId).orElseThrow(() -> new UsernameNotFoundException(memberId));
        findByMember.memberSecession();

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findByMember = memberRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
        if(findByMember.getDeleteYn().equals("Y")){
            throw new UsernameNotFoundException(username);
        }
        return new User(findByMember.getMemberId(),findByMember.getPassword(),authorities(findByMember.getAuthLevel()));
    }

    private Collection<? extends GrantedAuthority> authorities(String authLevel) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + authLevel));
    }

    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

package com.example.board.service.impl;

import com.example.board.domain.Member;
import com.example.board.repository.MemberRepository;
import com.example.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public String memberJoin(Member member) {
        Optional<Member> findByMember = memberRepository.findById(member.getMemberId());
        if(!findByMember.isEmpty()){
            throw new IllegalArgumentException("중복ID가 존재합니다.");
        }
        return memberRepository.save(member).getMemberId();
    }
}

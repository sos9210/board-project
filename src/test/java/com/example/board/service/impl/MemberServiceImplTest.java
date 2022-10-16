package com.example.board.service.impl;

import com.example.board.domain.Member;
import com.example.board.repository.MemberRepository;
import com.example.board.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    MemberRepository memberRepository;

    private MemberService getMemberService() {
        MemberService memberService = new MemberServiceImpl(memberRepository);
        return memberService;
    }

    @Test
    void 회원가입을하면_ID를_반환한다() {
        //given
        MemberService memberService = getMemberService();
        Member member = new Member("asd123","user11","asd123!@#","1", LocalDateTime.now(),"127.0.0.1");
        given(memberRepository.save(member)).willReturn(member);

        //when
        String memberId = memberService.memberJoin(member);

        //then
        Assertions.assertEquals(memberId,member.getMemberId());
        then(memberRepository).should(times(1)).save(member);
    }

    @Test
    void 회원가입시_중복ID_Exception() {
        //given
        MemberService memberService1 = getMemberService();
        Member member1 = new Member("asd123","user11","asd123!@#","1", LocalDateTime.now(),"127.0.0.1");
        given(memberRepository.save(member1)).willReturn(member1);
        String member1Id = memberService1.memberJoin(member1);

        //when
        MemberService memberService2 = getMemberService();
        Member member2 = new Member("asd123","user11","asd123!@#","1", LocalDateTime.now(),"127.0.0.1");
        when(memberRepository.findById(member2.getMemberId())).thenReturn(Optional.of(member2));

        //then
        then(memberRepository).should(never()).save(member2);
        Assertions.assertThrows(IllegalArgumentException.class, () -> memberService2.memberJoin(member2));
    }

}
package com.example.board.service.impl;

import com.example.board.domain.Member;
import com.example.board.dto.MemberDTO;
import com.example.board.enums.MemberAuthLevelEnum;
import com.example.board.repository.MemberRepository;
import com.example.board.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
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

    @Test
    void 회원_인증_성공() {
        //given
        MemberService memberService = getMemberService();
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encodePassword = delegatingPasswordEncoder.encode("asd123!@#");

        MemberDTO loginMember = new MemberDTO();
        loginMember.setMemberId("asd123123");
        loginMember.setPassword("asd123!@#");

        Member member = new Member("asd123123","user11",encodePassword, MemberAuthLevelEnum.USER.name(), LocalDateTime.now(),"127.0.0.1");
        given(memberRepository.findById(member.getMemberId())).willReturn(Optional.of(member));

        //when
        UserDetails user = memberService.loadUserByUsername("asd123123");

        //then
        Assertions.assertTrue(delegatingPasswordEncoder.matches(loginMember.getPassword(), user.getPassword()));
        Assertions.assertTrue(user.isEnabled());
    }

    @Test
    void 찾을_수_없는_회원_로그인_시도() {
        //given
        MemberService memberService = getMemberService();
        Member member = new Member("asd123", "user11", "Asd123!@#", MemberAuthLevelEnum.USER.name(), LocalDateTime.now(), "127.0.0.1");

        given(memberRepository.save(member)).willReturn(member);
        given(memberRepository.findById("asd123123")).willThrow(UsernameNotFoundException.class);
        given(memberRepository.findById("asd123")).willReturn(Optional.of(member));

        memberService.memberJoin(member);

        //when //then
        Assertions.assertThrows(UsernameNotFoundException.class ,() -> memberService.loadUserByUsername("asd123123"));
        Assertions.assertEquals("asd123",memberRepository.findById("asd123").get().getMemberId());

    }

}
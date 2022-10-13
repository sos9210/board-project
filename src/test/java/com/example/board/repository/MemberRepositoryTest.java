package com.example.board.repository;

import com.example.board.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원가입")
    @Test
    void 회원등록() {
        //given
        Member member = new Member("asd1","hong","pass","1", LocalDateTime.now(),"127.0.0.1");
        Member saveMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById("asd1").orElseGet(() -> new Member());

        //then
        Assertions.assertEquals(saveMember.getMemberId(), findMember.getMemberId());
        Assertions.assertEquals(saveMember, findMember);
    }


    @DisplayName("회원조회")
    @Test
    void 회원조회() {
        //given
        Member member = new Member("asd1","hong","pass","1", LocalDateTime.now(),"127.0.0.1");
        Member saveMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById("asd1").orElseGet(() -> new Member());

        //then
        Assertions.assertEquals(saveMember.getMemberId(), findMember.getMemberId());
        Assertions.assertEquals(saveMember.getMemberName(), findMember.getMemberName());
        Assertions.assertEquals(saveMember.getPassword(), findMember.getPassword());
        Assertions.assertEquals(saveMember.getAuthLevel(), findMember.getAuthLevel());
        Assertions.assertEquals(saveMember.getRegistDate(), findMember.getRegistDate());
        Assertions.assertEquals(saveMember.getRegistIp(), findMember.getRegistIp());
    }
}
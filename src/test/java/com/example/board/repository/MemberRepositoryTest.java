package com.example.board.repository;

import com.example.board.domain.Board;
import com.example.board.domain.BoardComment;
import com.example.board.domain.Member;
import com.example.board.dto.BoardCommentDTO;
import com.example.board.dto.MemberDTO;
import com.example.board.enums.MemberAuthLevelEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    EntityManager em;

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

    @Test
    void 회원목록조회() {
        //given
        Pageable pageable = PageRequest.of(0,10);

        for (int i = 0; i < 20; i++) {
            Member member = new Member("asd1"+i,"hong"+i,"pass","1", LocalDateTime.now(),"127.0.0.1");
            em.persist(member);
        }
        em.flush();
        em.clear();

        //when
        Page<MemberDTO> members = memberRepository.findMembers(pageable);


        //then
        Assertions.assertEquals(20L,members.getTotalElements());
        Assertions.assertEquals(10,members.getContent().size());
    }
}
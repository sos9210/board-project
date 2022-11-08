package com.example.board.repository;

import com.example.board.domain.Board;
import com.example.board.domain.BoardComment;
import com.example.board.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    CommentRepository commentRepository;

    @Test
    void 코멘트_등록() {
        //given
        Member member = new Member("asd1","hong","pass","1", LocalDateTime.now(),"127.0.0.1");
        Board board = new Board("title111","content222",member,"N",LocalDateTime.now(),"127.0.0.1");

        em.persist(member);
        em.persist(board);
        em.flush();

        BoardComment boardComment = new BoardComment("댓글내용입니다123123",member,board,"N",LocalDateTime.now(),"127.0.0.1");

        //when
        BoardComment savedComment = commentRepository.save(boardComment);
        em.flush();

        //then
        Assertions.assertEquals(savedComment.getBoard().getBoardSn(),board.getBoardSn());
        Assertions.assertEquals(savedComment.getMember().getMemberId(),member.getMemberId());
    }
}

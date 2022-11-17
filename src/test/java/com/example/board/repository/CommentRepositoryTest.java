package com.example.board.repository;

import com.example.board.domain.*;
import com.example.board.dto.BoardCommentDTO;
import com.example.board.dto.BoardDTO;
import com.example.board.dto.QBoardCommentDTO;
import com.example.board.dto.QBoardDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Assertions;
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
import java.util.List;
import java.util.Optional;

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
        Member member = new Member("asd1","hong","pass","1","N", LocalDateTime.now(),"127.0.0.1");
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

    @Test
    void 게시물_코멘트목록조회(){
        //given
        Pageable pageable = PageRequest.of(0,10);

        Member member = new Member("asd1","hong","pass","1", "N",LocalDateTime.now(),"127.0.0.1");
        em.persist(member);
        Board board = new Board("title","content",member,"N",LocalDateTime.now(),"127.0.0.1");
        em.persist(board);
        for (int i = 0; i < 20; i++) {
            BoardComment comment = new BoardComment("title"+i,member,board,"N",LocalDateTime.now(),"127.0.0.1");
            em.persist(comment);
        }
        em.flush();
        em.clear();

        //when
        BoardCommentDTO commentDTO = new BoardCommentDTO();
        commentDTO.setBoardSn(board.getBoardSn());
        Page<BoardCommentDTO> byCommentList = commentRepository.findByCommentList(commentDTO, pageable);


        //then
        Assertions.assertEquals(20L,byCommentList.getTotalElements());
        Assertions.assertEquals(10,byCommentList.getContent().size());
    }
    @Test
    void 게시물_코멘트상세조회(){
        //given
        Pageable pageable = PageRequest.of(0,10);

        Member member = new Member("asd1","hong","pass","1","N", LocalDateTime.now(),"127.0.0.1");
        em.persist(member);
        Board board = new Board("title","content",member,"N",LocalDateTime.now(),"127.0.0.1");
        em.persist(board);

        BoardComment comment = new BoardComment("title",member,board,"N",LocalDateTime.now(),"127.0.0.1");
        em.persist(comment);

        em.flush();
        em.clear();

        //when
        BoardCommentDTO commentDTO = new BoardCommentDTO();
        commentDTO.setBoardSn(board.getBoardSn());
        BoardComment findComment = commentRepository.findByBoardCommentSnAndBoardBoardSn(comment.getBoardCommentSn(),board.getBoardSn()).get();


        //then
        Assertions.assertEquals(findComment.getBoardCommentSn(),comment.getBoardCommentSn());
        Assertions.assertEquals(findComment.getBoard().getBoardSn(),board.getBoardSn());
    }

    @Test
    void 게시물_코멘트삭제() {
        //given
        Pageable pageable = PageRequest.of(0, 10);

        Member member = new Member("asd1", "hong", "pass", "1","N", LocalDateTime.now(), "127.0.0.1");
        em.persist(member);
        Board board = new Board("title", "content", member, "N", LocalDateTime.now(), "127.0.0.1");
        em.persist(board);

        BoardComment comment = new BoardComment("title", member, board, "N", LocalDateTime.now(), "127.0.0.1");
        em.persist(comment);

        //when
        BoardCommentDTO commentDTO = new BoardCommentDTO();

        commentDTO.setUpdateDate(LocalDateTime.now());
        commentDTO.setUpdateIp("127.0.0.1");
        commentDTO.setDeleteYn("Y");
        comment.deleteComment(commentDTO);

        em.flush();
        em.clear();

        //then
        BoardComment findComment = commentRepository.findByBoardCommentSnAndBoardBoardSn(comment.getBoardCommentSn(), board.getBoardSn()).get();

        Assertions.assertEquals(findComment.getDeleteYn(),"Y");

    }
}

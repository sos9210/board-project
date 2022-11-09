package com.example.board.service.impl;

import com.example.board.domain.Board;
import com.example.board.domain.BoardComment;
import com.example.board.domain.Member;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import com.example.board.service.CommentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    CommentRepository commentRepository;

    CommentService commentService;

    @BeforeEach
    private void getBoardService() {
        commentService = new CommentServiceImpl(commentRepository);
    }

    @Test
    void 코멘트_등록(){
        //given
        Member member = new Member("asd123","user11","asd123!@#","1", LocalDateTime.now(),"127.0.0.1");
        Board board = new Board("안녕하세요.","새로 가입한 홍길동 입니다.",member, "N",LocalDateTime.now(),"127.0.0.1");
        BoardComment boardComment = new BoardComment("sss",member,board,"N",LocalDateTime.now(),"127.0.0.1");

        ReflectionTestUtils.setField(boardComment,"boardCommentSn",1L);
        given(commentRepository.save(boardComment)).willReturn(boardComment);

        //when
        Long savedId = commentService.writeComment(boardComment);

        //then
        Assertions.assertEquals(savedId,1L);
    }
}
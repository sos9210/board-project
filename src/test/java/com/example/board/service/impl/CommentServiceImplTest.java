package com.example.board.service.impl;

import com.example.board.domain.Board;
import com.example.board.domain.BoardComment;
import com.example.board.domain.Member;
import com.example.board.dto.BoardCommentDTO;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.MemberRepository;
import com.example.board.service.CommentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    CommentRepository commentRepository;
    @Mock
    BoardRepository boardRepository;
    @Mock
    MemberRepository memberRepository;

    CommentService commentService;

    @BeforeEach
    private void getBoardService() {
        commentService = new CommentServiceImpl(commentRepository,memberRepository,boardRepository);
    }

    @Test
    void 코멘트_등록(){

        BoardCommentDTO dto = new BoardCommentDTO();
        dto.setBoardSn(33L);
        dto.setRegistIp("127.0.0.1");
        dto.setContent("이건 댓글입니다.");
        dto.setMemberId("asd123");

        //given
        Member member = new Member("asd123","user11","asd123!@#","1", LocalDateTime.now(),"127.0.0.1");
        Board board = new Board("안녕하세요.","새로 가입한 홍길동 입니다.",member, "N",LocalDateTime.now(),"127.0.0.1");

        given(memberRepository.findById(dto.getMemberId())).willReturn(Optional.of(member));
        given(boardRepository.findById(dto.getBoardSn())).willReturn(Optional.of(board));
        ReflectionTestUtils.setField(board,"boardSn",33L);

        BoardComment boardComment = new BoardComment(dto.getContent(),member,board,"N",LocalDateTime.now(),dto.getRegistIp());
        given(commentRepository.save(any(BoardComment.class))).willReturn(boardComment);
        ReflectionTestUtils.setField(boardComment,"boardCommentSn",1L);

        //when
        Long savedId = commentService.writeComment(dto);

        //then
        Assertions.assertEquals(savedId,1L);
    }

    @Test
    void 코멘트_수정() {
        //given
        BoardCommentDTO dto = new BoardCommentDTO();
        dto.setBoardSn(33L);
        dto.setBoardCommentSn(1L);
        dto.setUpdateIp("127.0.0.1");
        dto.setContent("이건 댓글입니다.");

        BoardComment boardComment = new BoardComment(dto.getContent(),new Member(),new Board(),"N",LocalDateTime.now(),dto.getRegistIp());
        ReflectionTestUtils.setField(boardComment,"boardCommentSn",1L);

        given(commentRepository.findByBoardCommentSnAndBoardBoardSn(dto.getBoardCommentSn(),dto.getBoardSn())).willReturn(Optional.of(boardComment));

        //when
        Long updateId = commentService.editComment(dto);

        //then
        Assertions.assertEquals(updateId,1L);
    }
    @Test
    void 코멘트_삭제() {
        //given
        BoardCommentDTO dto = new BoardCommentDTO();
        dto.setDeleteYn("Y");
        dto.setUpdateIp("127.0.0.1");

        BoardComment boardComment = new BoardComment("댓글123123",new Member(),new Board(),"Y",LocalDateTime.now(),"127.0.0.1");
        ReflectionTestUtils.setField(boardComment,"boardCommentSn",1L);

        given(commentRepository.findByBoardCommentSnAndBoardBoardSn(dto.getBoardCommentSn(),dto.getBoardSn())).willReturn(Optional.of(boardComment));

        //when
        //then
        Assertions.assertDoesNotThrow(() -> commentService.deleteComment(dto));
    }

}
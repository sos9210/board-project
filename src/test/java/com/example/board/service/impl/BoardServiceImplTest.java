package com.example.board.service.impl;

import com.example.board.domain.Board;
import com.example.board.domain.Member;
import com.example.board.repository.AttachFileRepository;
import com.example.board.repository.BoardRepository;
import com.example.board.service.BoardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartHttpServletRequest;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceImplTest {

    @Mock
    BoardRepository boardRepository;
    @Mock
    AttachFileRepository attachFileRepository;

    BoardService boardService;

    @BeforeEach
    private void getBoardService() {
       boardService = new BoardServiceImpl(boardRepository,attachFileRepository);
    }

    @Test
    void 게시글_등록() throws IOException {
        //given
        Member member = new Member("asd123","user11","asd123!@#","1", LocalDateTime.now(),"127.0.0.1");
        Board board = new Board("안녕하세요.","새로 가입한 홍길동 입니다.",member, "N",LocalDateTime.now(),"127.0.0.1");

        given(boardRepository.save(board)).willReturn(board);

        //when
        Long boardId = boardService.writeBoard(board,new MockMultipartHttpServletRequest());

        //then
        Assertions.assertEquals(board.getBoardSn(),boardId);
    }
}
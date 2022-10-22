package com.example.board.service.impl;

import com.example.board.domain.AttachFile;
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
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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
    @Test
    void 게시글_첨부파일_등록() throws IOException {
        //given
        Member member = new Member("asd123","user11","asd123!@#","1", LocalDateTime.now(),"127.0.0.1");
        Board board = new Board("안녕하세요.","새로 가입한 홍길동 입니다.",member, "N",LocalDateTime.now(),"127.0.0.1");
        ReflectionTestUtils.setField(board,"boardSn",1L);       //가짜 게시판순번 셋팅

        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();

        FileInputStream fileInputStream = new FileInputStream(System.getProperty("user.dir")+"/upload/test/test.txt");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain",fileInputStream );

        String[] originalFilenameAndExt = multipartFile.getOriginalFilename().split("\\.");
        String rootPath = System.getProperty("user.dir");

        request.addFile(multipartFile);

        given(boardRepository.save(board)).willReturn(board);

        //when
        List<AttachFile> fileSaves = ReflectionTestUtils.invokeMethod(boardService, "fileSave", board, request);
        Long boardId = boardService.writeBoard(board,request);

        //then
        Assertions.assertEquals(board.getBoardSn(),boardId);
        Assertions.assertEquals(fileSaves.get(0).getBoard().getBoardSn(), board.getBoardSn());
        Assertions.assertEquals(fileSaves.get(0).getRealFileName(), "test");
    }
}
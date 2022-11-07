package com.example.board.service;

import com.example.board.domain.AttachFile;
import com.example.board.domain.Board;
import com.example.board.dto.BoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;

public interface BoardService {
    BoardDTO viewBoard(Long boardSn);
    Long writeBoard(Board board, MultipartHttpServletRequest request) throws IOException;
    Page<BoardDTO> listForum(BoardDTO search, Pageable pageable);
    void deleteBoard(BoardDTO boardDTO);
    Long updateBoard(BoardDTO boardDTO, MultipartHttpServletRequest request);
    AttachFile findAttachFile(Long attachFileSn) ;
}

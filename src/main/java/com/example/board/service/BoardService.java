package com.example.board.service;

import com.example.board.domain.Board;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;

public interface BoardService {

    Long writeBoard(Board board, MultipartHttpServletRequest request) throws IOException;
}
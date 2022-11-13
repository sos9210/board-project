package com.example.board.service;

import com.example.board.domain.BoardComment;
import com.example.board.dto.BoardCommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentService {
    Long writeComment(BoardCommentDTO commentDTO);
    Page<BoardCommentDTO> commentList(BoardCommentDTO commentDTO, Pageable pageable);
    Long editComment(BoardCommentDTO commentDTO);
    void deleteComment(BoardCommentDTO commentDTO);
}

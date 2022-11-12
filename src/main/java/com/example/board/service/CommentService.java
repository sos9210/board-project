package com.example.board.service;

import com.example.board.dto.BoardCommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Long writeComment(BoardCommentDTO commentDTO);
    Page<BoardCommentDTO> commentList(BoardCommentDTO commentDTO, Pageable pageable);
}

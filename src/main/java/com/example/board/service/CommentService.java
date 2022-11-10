package com.example.board.service;

import com.example.board.dto.BoardCommentDTO;

public interface CommentService {
    Long writeComment(BoardCommentDTO boardComment);
}

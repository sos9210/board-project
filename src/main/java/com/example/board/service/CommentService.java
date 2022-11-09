package com.example.board.service;

import com.example.board.domain.BoardComment;

public interface CommentService {
    Long writeComment(BoardComment boardComment);
}

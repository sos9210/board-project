package com.example.board.service.impl;

import com.example.board.domain.BoardComment;
import com.example.board.repository.CommentRepository;
import com.example.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Long writeComment(BoardComment boardComment) {
        return commentRepository.save(boardComment).getBoardCommentSn();
    }
}

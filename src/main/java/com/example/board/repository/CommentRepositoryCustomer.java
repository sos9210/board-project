package com.example.board.repository;

import com.example.board.dto.BoardCommentDTO;
import com.example.board.dto.BoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustomer {
    Page<BoardCommentDTO> findByCommentList(BoardCommentDTO commentDTO, Pageable pageable);
}

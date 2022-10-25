package com.example.board.repository;

import com.example.board.dto.BoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<BoardDTO> findByBoardList(BoardDTO board, Pageable pageable);
}

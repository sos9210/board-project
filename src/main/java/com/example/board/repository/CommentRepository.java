package com.example.board.repository;

import com.example.board.domain.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<BoardComment,Long>,CommentRepositoryCustomer {
    Optional<BoardComment> findByBoardCommentSnAndBoardBoardSn(Long boardCommentSn, Long boardSn);
}

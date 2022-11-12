package com.example.board.repository;

import com.example.board.domain.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<BoardComment,Long>,CommentRepositoryCustomer {
}

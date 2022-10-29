package com.example.board.repository;

import com.example.board.domain.Board;
import com.example.board.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>,BoardRepositoryCustom {
    Optional<Board> findByBoardSnAndMember(Long id, Member member);
    Optional<Board> findByBoardSnAndDeleteYn(Long id, String deleteYn);
}

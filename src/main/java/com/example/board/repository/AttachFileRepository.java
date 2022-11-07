package com.example.board.repository;

import com.example.board.domain.AttachFile;
import com.example.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachFileRepository extends JpaRepository<AttachFile,Long> {
    Optional<AttachFile> findByBoard(Board board);
    void deleteByBoard(Board board);
}

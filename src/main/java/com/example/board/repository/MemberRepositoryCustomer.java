package com.example.board.repository;

import com.example.board.dto.MemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustomer {
    Page<MemberDTO> findMembers(Pageable pageable);
}

package com.example.board.repository;

import com.example.board.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String>,MemberRepositoryCustomer {
    Optional<Member> findByMemberIdAndPassword(String memberId, String password);
}

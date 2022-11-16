package com.example.board.repository;

import com.example.board.domain.Member;
import com.example.board.domain.QMember;
import com.example.board.dto.BoardCommentDTO;
import com.example.board.dto.MemberDTO;
import com.example.board.dto.QBoardCommentDTO;
import com.example.board.dto.QMemberDTO;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class MemberRepositoryCustomerImpl implements MemberRepositoryCustomer{

    private final JPAQueryFactory queryFactory;
    public MemberRepositoryCustomerImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MemberDTO> findMembers(Pageable pageable) {
        QMember member = QMember.member;

        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member);


        List<MemberDTO> memberList = queryFactory
                .select(
                        new QMemberDTO(
                                member.memberId,
                                member.memberName,
                                member.registDate
                        )
                )
                .from(member)
                .orderBy(member.registDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(memberList, pageable,  countQuery::fetchOne);
    }
}

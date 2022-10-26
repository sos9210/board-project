package com.example.board.repository;

import com.example.board.domain.QBoard;
import com.example.board.dto.BoardDTO;
import com.example.board.dto.QBoardDTO;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.board.domain.QBoard.*;

@Repository
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardDTO> findByBoardList(BoardDTO search, Pageable pageable) {
        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(
                        subjectEq(search.getSubject()),
                        contentEq(search.getContent())
                );


        List<BoardDTO> boardList = queryFactory
                .select(new QBoardDTO(
                        board.boardSn,
                        board.subject,
                        board.content,
                        board.member.memberId,
                        board.registDate
                ))
                .from(board)
                .where(
                        subjectEq(search.getSubject()),
                        contentEq(search.getContent())
                )
                .orderBy(board.registDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(boardList, pageable,  countQuery::fetchOne);
    }

    private Predicate subjectEq(String subject){
        return StringUtils.hasText(subject) ? board.subject.eq(subject) : null;
    }
    private Predicate contentEq(String content){
        return StringUtils.hasText(content) ? board.content.eq(content) : null;
    }
}

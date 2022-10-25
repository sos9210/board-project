package com.example.board.repository;

import com.example.board.domain.Board;
import com.example.board.domain.QBoard;
import com.example.board.dto.BoardDTO;
import com.example.board.dto.QBoardDTO;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


@Repository
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardDTO> findByBoardList(BoardDTO search, Pageable pageable) {
        QBoard board = QBoard.board;
        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(
                        board.subject.eq(search.getSubject()).or(board.content.eq(search.getContent()))
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
                        board.subject.eq(search.getSubject()).or(board.content.eq(search.getContent()))
                )
                .orderBy(board.registDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(boardList, pageable,  countQuery::fetchOne);
    }
}

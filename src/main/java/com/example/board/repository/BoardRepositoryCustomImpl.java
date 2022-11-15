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
                        searchKeyword(search),
                        deleteYnEq("N")
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
                        searchKeyword(search),
                        deleteYnEq("N")
                )
                .orderBy(board.registDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(boardList, pageable,  countQuery::fetchOne);
    }

    private Predicate searchKeyword(BoardDTO search){
        //전체
        if(search.getSearchCondition() == null || search.getSearchCondition().equals("0")){
            return StringUtils.hasText(search.getSearchKeyword()) ?
                board.subject.contains(search.getSearchKeyword()).or(board.content.contains(search.getSearchKeyword())) : null;
        }else if(search.getSearchCondition().equals("1")){    //제목
            return StringUtils.hasText(search.getSearchKeyword()) ? board.subject.contains(search.getSearchKeyword()) : null;
        }else if(search.getSearchCondition().equals("2")){//내용
            return StringUtils.hasText(search.getSearchKeyword()) ? board.content.contains(search.getSearchKeyword()) : null;
        }
        return null;
    }
    private Predicate deleteYnEq(String deleteYn){
        return board.deleteYn.eq("N");
    }
}

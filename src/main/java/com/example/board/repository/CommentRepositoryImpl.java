package com.example.board.repository;

import com.example.board.domain.QBoardComment;
import com.example.board.dto.BoardCommentDTO;
import com.example.board.dto.BoardDTO;
import com.example.board.dto.QBoardCommentDTO;
import com.example.board.dto.QBoardDTO;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.board.domain.QBoard.board;

@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustomer{

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardCommentDTO> findByCommentList(BoardCommentDTO commentDTO, Pageable pageable) {
        QBoardComment comment = QBoardComment.boardComment;

        JPAQuery<Long> countQuery = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.board.boardSn.eq(commentDTO.getBoardSn())
                        .and(comment.deleteYn.eq("N")));


        List<BoardCommentDTO> commentList = queryFactory
                .select(new QBoardCommentDTO(
                        comment.member.memberId,
                        comment.board.boardSn,
                        comment.content,
                        comment.deleteYn,
                        comment.registDate
                ))
                .from(comment)
                .where(
                        comment.board.boardSn.eq(commentDTO.getBoardSn())
                                .and(comment.deleteYn.eq("N"))
                )
                .orderBy(comment.registDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(commentList, pageable,  countQuery::fetchOne);
    }
}

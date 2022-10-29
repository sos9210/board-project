package com.example.board.repository;

import com.example.board.domain.Board;
import com.example.board.domain.Member;
import com.example.board.domain.QBoard;
import com.example.board.dto.BoardDTO;
import com.example.board.dto.QBoardDTO;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager em;

    private JPAQueryFactory queryFactory;



    @Test
    void 게시글_등록_상세조회(){
        //given
        Member member = new Member("asd1","hong","pass","1", LocalDateTime.now(),"127.0.0.1");
        Board board = new Board("title111","content222",member,"N",LocalDateTime.now(),"127.0.0.1");

        em.persist(board);

        //when
        Board findBoard = boardRepository.findById(board.getBoardSn()).orElseGet(Board::new);

        //then
        Assertions.assertEquals(board,findBoard);
        Assertions.assertEquals(board.getBoardSn(),findBoard.getBoardSn());
    }

    @Test
    void 게시글_삭제() {
        //given
        Member member = new Member("asd1","hong","pass","1", LocalDateTime.now(),"127.0.0.1");
        Board board = new Board("title111","content222",member,"N",LocalDateTime.now(),"127.0.0.1");

        em.persist(member);
        em.persist(board);

        //when
        Board findBoard = boardRepository.findByBoardSnAndMember(board.getBoardSn(),member).orElseGet(Board::new);
        findBoard.boardDelete();

        em.flush();
        em.clear();

        //then
        Board deleteBoard = boardRepository.findByBoardSnAndMember(board.getBoardSn(),member).orElseGet(Board::new);

        Assertions.assertEquals(deleteBoard.getDeleteYn(),"Y");

    }

    @Test
    void 전체목록조회(){
        //given
        queryFactory = new JPAQueryFactory(em);
        Pageable pageable = PageRequest.of(0,10);

        Member member = new Member("asd1","hong","pass","1", LocalDateTime.now(),"127.0.0.1");
        em.persist(member);

        for (int i = 0; i < 20; i++) {
            Board board = new Board("title"+i,"content"+i,member,"N",LocalDateTime.now(),"127.0.0.1");
            em.persist(board);
        }
        em.flush();
        em.clear();

        //when
        QBoard board = QBoard.board;
        Long countQuery = queryFactory
                .select(board.count())
                .from(board).fetchOne();

        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        List<BoardDTO> boardList = queryFactory
                .select(new QBoardDTO(
                        board.boardSn,
                        board.subject,
                        board.content,
                        board.member.memberId,
                        board.registDate
                ))
                .from(board)
                .orderBy(board.registDate.desc())
                .offset(offset)
                .limit(pageSize)
                .fetch();


        //then
        Assertions.assertEquals(20L,countQuery);
        Assertions.assertEquals(10,boardList.size());
    }
}
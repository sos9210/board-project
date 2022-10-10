package com.example.board.repository;

import com.example.board.domain.Board;
import com.example.board.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @DisplayName("게시글 등록")
    @Test
    void saveBoard(){
        //given
        Member member = new Member("asd1","hong","pass","1", LocalDateTime.now(),"127.0.0.1");
        Board board = new Board("title111","content222",member,"N",LocalDateTime.now(),"127.0.0.1");

        Board saveBoard = boardRepository.save(board);

        //when
        Board findBoard = boardRepository.findById(saveBoard.getBoardSn()).orElseGet(Board::new);

        //then
        Assertions.assertEquals(saveBoard,findBoard);
        Assertions.assertEquals(saveBoard.getBoardSn(),findBoard.getBoardSn());
    }

    @DisplayName("전체목록조회")
    @Test
    void findAll(){
        //given
        Member member = new Member("asd1","hong","pass","1", LocalDateTime.now(),"127.0.0.1");
        memberRepository.save(member);
        em.flush();

        for (int i = 0; i < 20; i++) {
            Board board = new Board("title"+i,"content"+i,member,"N",LocalDateTime.now(),"127.0.0.1");
            boardRepository.save(board);
        }
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by(Sort.Direction.DESC,"boardSn"));

        //when
        Page<Board> boardPage = boardRepository.findAll(pageRequest);

        //then
        List<Board> boardList = boardPage.getContent();
        long totalCnt = boardPage.getTotalElements();
        long totalPages = boardPage.getTotalPages();
        Assertions.assertEquals(10,boardList.size());
        Assertions.assertEquals(20,totalCnt);
        Assertions.assertEquals(2,totalPages);
    }
}
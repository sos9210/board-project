package com.example.board.repository;

import com.example.board.domain.AttachFile;
import com.example.board.domain.Board;
import com.example.board.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class AttachFileRepositoryTest {

    @Autowired
    AttachFileRepository fileRepository;
    @Autowired
    EntityManager em;

    @Test
    void 파일_저장() {
        //given
        Member member = new Member("asd123","user11","asd123!@#","1", LocalDateTime.now(),"127.0.0.1");
        Board board = new Board("안녕하세요.","새로 가입한 홍길동 입니다.",member, "N",LocalDateTime.now(),"127.0.0.1");
        em.persist(member);
        em.persist(board);

        //when
        AttachFile attachFile = new AttachFile("reafile.docs","ASDF-1234.docs","docs",12312,"/data/upload",board, LocalDateTime.now(),"127.0.0.1");
        AttachFile savedFile = fileRepository.save(attachFile);

        //then
        AttachFile findFile = fileRepository.findById(savedFile.getAttachFileSn()).get();
        Assertions.assertEquals(findFile.getRealFileName(),savedFile.getRealFileName());
        Assertions.assertEquals(findFile.getStoredFileName(),savedFile.getStoredFileName());
    }
}
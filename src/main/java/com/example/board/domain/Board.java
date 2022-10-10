package com.example.board.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter
@NoArgsConstructor
public class Board {

    public Board(String subject, String content, Member member, String deleteYn, LocalDateTime registDate, String registIp) {
        this.subject = subject;
        this.content = content;
        this.member = member;
        this.deleteYn = deleteYn;
        this.registDate = registDate;
        this.registIp = registIp;
    }

    @GeneratedValue @Id
    private Long boardSn;                   //게시판순번

    private String subject;                 //제목
    private String content;                 //내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;                  //회원

    private String deleteYn;                //삭제여부
    private LocalDateTime registDate;       //등록일자
    private String registIp;                //등록IP
    private LocalDateTime updateDate;       //수정일자
    private String updateIp;                //수정IP

}

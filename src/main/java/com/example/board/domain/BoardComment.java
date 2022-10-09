package com.example.board.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter
public class BoardComment {

    @GeneratedValue @Id
    private Long boardCommentSn;

    private String content;                 //내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;                  //회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_sn")
    private Board board;                    //게시판

    private String deleteYn;                //삭제여부

    private LocalDateTime registDate;       //등록일자
    private String registIp;                //등록IP
    private LocalDateTime updateDate;       //수정일자
    private String updateIp;                //수정IP
}

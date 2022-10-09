package com.example.board.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter
public class AttachFile {

    @GeneratedValue @Id
    private Long attachFileSn;              //첨부파일순번

    private String realFileName;            //실제파일명
    private String storedFileName;          //저장파일명
    private String extension;               //확장자
    private long sized;                     //크기
    private String savedPath;               //저장경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_sn")
    private Board board;                    //게시판

    private LocalDateTime registDate;       //등록일자
    private String registIp;                //등록IP
}

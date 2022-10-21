package com.example.board.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter
public class AttachFile {
    public AttachFile(String realFileName, String storedFileName, String extension, long sized, String savedPath, Board board, LocalDateTime registDate, String registIp) {
        this.realFileName = realFileName;
        this.storedFileName = storedFileName;
        this.extension = extension;
        this.sized = sized;
        this.savedPath = savedPath;
        this.board = board;
        this.registDate = registDate;
        this.registIp = registIp;
    }

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

package com.example.board.domain;

import com.example.board.dto.BoardCommentDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity @Getter
public class BoardComment {
    public BoardComment(String content, Member member, Board board, String deleteYn, LocalDateTime registDate, String registIp) {
        this.content = content;
        this.member = member;
        this.board = board;
        this.deleteYn = deleteYn;
        this.registDate = registDate;
        this.registIp = registIp;
    }

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

    public void editComment(BoardCommentDTO dto) {
        this.updateDate = dto.getUpdateDate();
        this.updateIp = dto.getUpdateIp();
        this.content = dto.getContent();
    }

    public void deleteComment(BoardCommentDTO dto) {
        this.deleteYn = dto.getDeleteYn();
        this.updateDate = dto.getUpdateDate();
        this.updateIp = dto.getUpdateIp();
    }
}

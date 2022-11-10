package com.example.board.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
public class BoardCommentDTO {

    private Long boardCommentSn;

    private String memberId;                //회원ID
    private Long BoardSn;                   //게시글순번

    @NotNull
    @NotBlank
    private String content;                 //내용
    private String deleteYn;                //삭제여부
    private LocalDateTime registDate;       //등록일자
    private String registIp;                //등록IP
    private LocalDateTime updateDate;       //수정일자
    private String updateIp;                //수정IP
}

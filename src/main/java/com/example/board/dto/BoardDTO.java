package com.example.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
public class BoardDTO {

    private Long boardSn;                   //게시판순번
    @NotNull @NotBlank
    private String subject;                 //제목
    @NotNull @NotBlank
    private String content;                 //내용

    private LocalDateTime registDate;       //등록일자
    private String registIp;                //등록IP

}

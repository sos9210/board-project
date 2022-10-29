package com.example.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @Setter
public class BoardDTO {

    private Long boardSn;                   //게시판순번
    @NotNull @NotBlank
    private String subject;                 //제목
    @NotNull @NotBlank
    private String content;                 //내용

    private String memberId;                //작성자ID
    private String deleteYn;                //삭제여부

    private LocalDateTime registDate;       //등록일자
    private String registIp;                //등록IP
    private LocalDateTime updateDate;       //수정일자
    private String updateIp;                //수정IP

    @QueryProjection
    public BoardDTO(Long boardSn, String subject, String content,String memberId , LocalDateTime registDate) {
        this.boardSn = boardSn;
        this.subject = subject;
        this.content = content;
        this.memberId = memberId;
        this.registDate = registDate;
    }
}

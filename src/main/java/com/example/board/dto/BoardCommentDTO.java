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
public class BoardCommentDTO {
    @QueryProjection
    public BoardCommentDTO(Long boardCommentSn,String memberId, Long boardSn, String content, String deleteYn, LocalDateTime registDate) {
        this.boardCommentSn = boardCommentSn;
        this.memberId = memberId;
        this.BoardSn = boardSn;
        this.content = content;
        this.deleteYn = deleteYn;
        this.registDate = registDate;
    }

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

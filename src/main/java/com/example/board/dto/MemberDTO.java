package com.example.board.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class MemberDTO {
    @NotBlank @NotNull
    @Size(min = 6, max = 20)
    private String memberId;                //회원아이디
    @NotBlank @NotNull
    private String memberName;              //회원이름
    // 최소 8자리에서 최대20자리 숫자, 문자, 특수문자 각각 1개 이상
    @NotNull @NotBlank @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$")
    private String password;                //암호
    private String authLevel;               //권한등급
    private LocalDateTime registDate;       //등록일자
    private String registIp;                //등록IP
    private LocalDateTime updateDate;       //수정일자
    private String updateIp;                //수정IP
}

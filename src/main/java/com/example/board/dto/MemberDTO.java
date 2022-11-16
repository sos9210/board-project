package com.example.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemberDTO {
    @NotBlank(message = "{error.required.input}") @NotNull(message = "{error.required.input}")
    @Size(min = 6, max = 20, message = "{error.size.input}")
    private String memberId;                //회원아이디

    @NotBlank(message = "{error.required.input}") @NotNull(message = "{error.required.input}")
    private String memberName;              //회원이름

    // 최소 8자리에서 최대20자리 숫자, 문자, 특수문자 각각 1개 이상
    @NotNull(message = "{error.required.input}") @NotBlank(message = "{error.required.input}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$", message = "{error.pattern.password}")
    private String password;                //암호
    private String authLevel;               //권한등급
    private LocalDateTime registDate;       //등록일자
    private String registIp;                //등록IP
    private LocalDateTime updateDate;       //수정일자
    private String updateIp;                //수정IP

    @QueryProjection
    public MemberDTO(String memberId, String memberName,LocalDateTime registDate) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.registDate = registDate;
    }
}

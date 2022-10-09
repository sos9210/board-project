package com.example.board.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "MEMBERS")
@Entity @Getter
public class Member {

    @Id
    private String memberId;                //회원아이디

    private String memberName;              //회원이름
    private String password;                //암호
    private String authLevel;               //권한등급
    private LocalDateTime registDate;       //등록일자
    private String registIp;                //등록IP
    private LocalDateTime updateDate;       //수정일자
    private String updateIp;                //수정IP
}

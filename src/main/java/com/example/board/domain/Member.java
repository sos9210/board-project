package com.example.board.domain;

import com.example.board.dto.MemberDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "MEMBERS")
@Entity @Getter
public class Member {
    public Member() {
    }

    public Member(String memberId, String memberName, String password, String authLevel, String deleteYn,LocalDateTime registDate, String registIp) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.password = password;
        this.authLevel = authLevel;
        this.deleteYn = deleteYn;
        this.registDate = registDate;
        this.registIp = registIp;
    }

    @Id
    private String memberId;                //회원아이디

    private String memberName;              //회원이름
    private String password;                //암호
    private String authLevel;               //권한등급

    private String deleteYn;                //탈퇴여부
    private LocalDateTime registDate;       //등록일자
    private String registIp;                //등록IP
    private LocalDateTime updateDate;       //수정일자
    private String updateIp;                //수정IP

    public void encodePassword(String encodePassword) {
        this.password = encodePassword;
    }

    public void memberEdit(MemberDTO dto){
        this.password = dto.getPassword();
        this.memberName = dto.getMemberName();
    }

    public void memberSecession() {
        this.deleteYn = "Y";
    }
}

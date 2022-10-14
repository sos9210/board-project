package com.example.board.controller;

import com.example.board.domain.Member;
import com.example.board.dto.MemberDTO;
import com.example.board.enums.MemberAuthLevelEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
public class LoginController {

    @GetMapping("board/user/login")
    public String loginForm() {
        return "view/loginForm";
    }

    @GetMapping("board/user/member-join")
    public String memberJoinForm() {
        return "view/memberJoinForm";
    }

    @PostMapping("board/user/member-join")
    public String memberJoin(HttpServletRequest request,MemberDTO dto) {
        dto.setAuthLevel(MemberAuthLevelEnum.USER.name());
        Member member = new Member(dto.getMemberId(),dto.getMemberName(),dto.getPassword(),dto.getAuthLevel(), LocalDateTime.now(),request.getRemoteAddr());


        return "";
    }
}

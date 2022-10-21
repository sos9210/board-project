package com.example.board.controller;

import com.example.board.domain.Member;
import com.example.board.dto.MemberDTO;
import com.example.board.enums.MemberAuthLevelEnum;
import com.example.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller @Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("board/user/login")
    public String memberLoginForm() {
        return "view/loginForm";
    }

    @PostMapping("board/user/login")
    public String memberLogin(MemberDTO memberDTO) {
        try{
            UserDetails userDetails = memberService.loadUserByUsername(memberDTO.getMemberId());

            log.info("사용자 정보 {}",userDetails);
        }catch (UsernameNotFoundException e){
            log.info("사용자 정보를 찾을 수 없습니다.",e);
            throw e;
        }
        return "view/loginForm";
    }

    @GetMapping("board/*/logout")
    public String memberLogout(Authentication auth, HttpServletRequest request, HttpServletResponse response) {
        if(auth != null) {
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }
        return "redirect:/board/user/login";
    }

    @GetMapping("board/user/member-join")
    public String memberJoinForm(MemberDTO dto) {
        return "view/memberJoinForm";
    }

    @PostMapping("board/user/member-join")
    public String memberJoin(@Valid MemberDTO dto, Errors error,HttpServletRequest request) {
        if(error.hasErrors()){
            log.info("formError : {}",error.getFieldError().getDefaultMessage());
            return "view/memberJoinForm";
        }
        dto.setAuthLevel(MemberAuthLevelEnum.USER.name());
        Member member = new Member(dto.getMemberId(),dto.getMemberName(),dto.getPassword(),dto.getAuthLevel(), LocalDateTime.now(),request.getRemoteAddr());

        try{
            memberService.joinMember(member);
        }catch(IllegalArgumentException ex){
            error.rejectValue("memberId","error.duplication.id");
            return "view/memberJoinForm";
        }

        return "redirect:/board/user/login";
    }
}

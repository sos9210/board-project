package com.example.board.controller;

import com.example.board.domain.Member;
import com.example.board.dto.MemberDTO;
import com.example.board.enums.MemberAuthLevelEnum;
import com.example.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Locale;

@Controller @Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private final MessageSource messageSource;

    @GetMapping("/board/user/login")
    public String memberLoginForm() {
        return "view/loginForm";
    }

    @GetMapping("/board/*/logout")
    public String memberLogout(Authentication auth, HttpServletRequest request, HttpServletResponse response) {
        if(auth != null) {
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }
        return "redirect:/board/user/login";
    }

    @GetMapping("/board/user/member-join")
    public String memberJoinForm(MemberDTO dto) {
        return "view/memberJoinForm";
    }

    @GetMapping("/board/user/member-edit")
    public String memberEditForm(@AuthenticationPrincipal User user,MemberDTO dto, Model model) {

        String userId = user.getUsername();
        Member member = memberService.findMember(userId);

        dto.setMemberName(member.getMemberName());
        model.addAttribute("memberDTO",dto);

        return "view/memberEditForm";
    }
    @PostMapping("/board/user/member-edit")
    public String memberEdit(@ModelAttribute("memberDTO") @Valid MemberDTO member, Errors error, @AuthenticationPrincipal User user, Model model) {
        if(error.hasErrors() && !error.getFieldError().getField().equals("memberId")){
            log.info("formError : {}",error.getFieldError().getDefaultMessage());
            return "view/memberEditForm";
        }

        String rawPassword = member.getPassword();

        member.setMemberId(user.getUsername());
        Member editMember = memberService.editMember(member);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(editMember.getMemberId(), rawPassword));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/board/user/forums";
    }

    @PostMapping("/board/user/member-join")
    public String memberJoin(@Valid MemberDTO dto, Errors error,HttpServletRequest request) {
        if(error.hasErrors()){
            log.info("formError : {}",error.getFieldError().getDefaultMessage());
            return "view/memberJoinForm";
        }
        dto.setAuthLevel(MemberAuthLevelEnum.USER.name());
        Member member = new Member(dto.getMemberId(),dto.getMemberName(),dto.getPassword(),dto.getAuthLevel(), "N",LocalDateTime.now(),request.getRemoteAddr());

        try{
            memberService.joinMember(member);
        }catch(IllegalArgumentException ex){
            error.rejectValue("memberId","error.duplication.id");
            return "view/memberJoinForm";
        }

        return "redirect:/board/user/login";
    }

    @ResponseBody
    @DeleteMapping("/board/user/member-secession")
    public ResponseEntity<String> memberSecession(@AuthenticationPrincipal User user,Authentication auth,HttpServletRequest request,HttpServletResponse response){
        if(user == null) {
            String message = messageSource.getMessage("error.request.common", null, Locale.getDefault());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
        }

        String username = user.getUsername();
        memberService.memberSecession(username);
        String message = messageSource.getMessage("secession.success.member", null, Locale.getDefault());

        new SecurityContextLogoutHandler().logout(request,response,auth);

        return ResponseEntity.ok(message);
    }
}

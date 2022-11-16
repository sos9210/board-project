package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.MemberDTO;
import com.example.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class MemberManagementController {

    private final MemberService memberService;

    @GetMapping("/board/admin/members")
    public String managementMembers(Pageable pageable, Model model){

        Page<MemberDTO> memberDTOPage = memberService.findMembers(pageable);
        long totalElements = memberDTOPage.getTotalElements();
        int startPage = Math.max(1,memberDTOPage.getPageable().getPageNumber() - pageable.getPageSize());
        int endPage = Math.min(memberDTOPage.getTotalPages(), memberDTOPage.getPageable().getPageNumber() + pageable.getPageSize());
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("pageable",memberDTOPage.getPageable());
        model.addAttribute("page",memberDTOPage);
        model.addAttribute("memberList",memberDTOPage.getContent());
        model.addAttribute("totalElements",totalElements);

        return "view/memberList";
    }
}

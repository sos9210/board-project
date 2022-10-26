package com.example.board.controller;

import com.example.board.domain.Board;
import com.example.board.domain.Member;
import com.example.board.dto.BoardDTO;
import com.example.board.service.BoardService;
import com.example.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller @Slf4j
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    @GetMapping("/board/user/forums")
    public String boardForums(BoardDTO dto, Pageable pageable, Model model) {
        Page<BoardDTO> boardDTOPage = boardService.listForum(dto, pageable);
        int totalPages = boardDTOPage.getTotalPages();
        model.addAttribute("pageable",boardDTOPage.getPageable());
        model.addAttribute("boardList",boardDTOPage.getContent());
        model.addAttribute("totalPages",totalPages);
        return "view/boardForums";
    }

    @GetMapping("/board/user/forum/write")
    public String boardWriteForm() {
        return "view/boardWriteForm";
    }

    @PostMapping("/board/user/forum/write")
    public String boardWrite(@Valid BoardDTO dto, Errors errors, MultipartHttpServletRequest request,
                             Principal principal, Model model) {
        if(errors.hasErrors()){
            return "view/boardWriteForm";
        }

        try {
            Member findMember = memberService.findMember(principal.getName());
            Board board = new Board(dto.getSubject(),dto.getContent(),findMember, "N",LocalDateTime.now(),request.getRemoteAddr());
            Long boardId = boardService.writeBoard(board,request);
            return "redirect:/board/user/forum/"+boardId;

        }catch (IllegalArgumentException e){
            log.info("사용자 세션 오류입니다  : {}", e);
            return "redirect:/board/user/login";
        }catch (IOException e) {
            model.addAttribute("message","파일 업로드 요청 오류입니다.");
            return "view/boardWriteForm";
        }
    }

    @GetMapping("/board/user/forum/{boardSn}")
    public String boardView(@PathVariable("boardSn") Long boardSn, Model model) {
        Board board = boardService.viewBoard(boardSn);
        model.addAttribute("view",board);
        return "view/boardView";
    }
}

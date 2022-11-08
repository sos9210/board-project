package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class CommentController {

    @PostMapping("/board/user/forum/comment/{boardSn}")
    public String commentWrite(BoardDTO dto, Pageable pageable, Model model,  @AuthenticationPrincipal User user){

        if(user == null) {
            log.info("사용자 세션 오류입니다");
            return "redirect:/board/user/login";
        }



        return "view/boardView";
    }
}

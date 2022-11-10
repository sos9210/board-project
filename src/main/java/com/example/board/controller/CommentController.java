package com.example.board.controller;

import com.example.board.domain.BoardComment;
import com.example.board.dto.BoardCommentDTO;
import com.example.board.dto.BoardDTO;
import com.example.board.service.BoardService;
import com.example.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;
    private final MessageSource messageSource;

    @PostMapping("/board/user/forum/comment/{boardSn}")
    public String commentWrite(BoardCommentDTO comment, Errors errors, RedirectAttributes rttr, HttpServletRequest request,
                               @PathVariable("boardSn") Long boardSn, Model model , @AuthenticationPrincipal User user){
        if(user == null) {
            log.info("사용자 세션 오류입니다");
            return "redirect:/board/user/login";
        }
        if(errors.hasErrors()){
            BoardDTO boardDTO = boardService.viewBoard(boardSn);
            model.addAttribute("view",boardDTO);
            model.addAttribute("comment",new BoardComment());
            return "view/boardView";
        }
        comment.setMemberId(user.getUsername());
        comment.setBoardSn(boardSn);
        comment.setRegistIp(request.getRemoteAddr());
        Long savedId = commentService.writeComment(comment);

        if(savedId == null || savedId == 0){
            rttr.addFlashAttribute("message",messageSource.getMessage("error.request.common", null, Locale.getDefault()));
        }

        return "redirect:/board/user/forum/"+boardSn;
    }

}

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

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

    @PostMapping("/board/user/forum/comment/{boardSn}/{boardCommentSn}")
    public String commentEdit(BoardCommentDTO comment, Errors errors, HttpServletRequest request,
                               @PathVariable("boardSn") Long boardSn,@PathVariable("boardCommentSn") Long boardCommentSn, Model model , @AuthenticationPrincipal User user) {
        if (user == null) {
            log.info("사용자 세션 오류입니다");
            return "redirect:/board/user/login";
        }
        if (errors.hasErrors()) {
            BoardDTO boardDTO = boardService.viewBoard(boardSn);
            model.addAttribute("view", boardDTO);
            model.addAttribute("comment", new BoardComment());
            return "view/boardView";
        }

        comment.setBoardSn(boardSn);
        comment.setBoardCommentSn(boardCommentSn);
        comment.setUpdateIp(request.getRemoteAddr());
        comment.setUpdateDate(LocalDateTime.now());

        commentService.editComment(comment);

        return "redirect:/board/user/forum/" + boardSn;
    }

    @ResponseBody
    @DeleteMapping("/board/user/forum/comment/{boardSn}/{boardCommentSn}")
    public ResponseEntity<String> commentDelete(@PathVariable("boardSn") Long boardSn, @PathVariable("boardCommentSn") Long boardCommentSn,
                                                HttpServletRequest request, @AuthenticationPrincipal User user) {
        if (user == null) {
            log.info("사용자 세션 오류입니다");
            String message = messageSource.getMessage("error.request.common", null, Locale.getDefault());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
        }
        BoardCommentDTO comment = new BoardCommentDTO();
        comment.setBoardSn(boardSn);
        comment.setBoardCommentSn(boardCommentSn);
        comment.setUpdateIp(request.getRemoteAddr());

        commentService.deleteComment(comment);

        String message = messageSource.getMessage("delete.success.common", null, Locale.getDefault());
        return ResponseEntity.ok(message);
    }
}

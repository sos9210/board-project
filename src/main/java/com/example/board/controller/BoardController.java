package com.example.board.controller;

import com.example.board.domain.AttachFile;
import com.example.board.domain.Board;
import com.example.board.domain.BoardComment;
import com.example.board.domain.Member;
import com.example.board.dto.BoardDTO;
import com.example.board.service.BoardService;
import com.example.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Controller @Slf4j
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final MessageSource messageSource;

    @GetMapping("/board/user/forums")
    public String boardForums(BoardDTO dto, Pageable pageable, Model model) {
        Page<BoardDTO> boardDTOPage = boardService.listForum(dto, pageable);
        long totalElements = boardDTOPage.getTotalElements();
        int startPage = Math.max(1,boardDTOPage.getPageable().getPageNumber() - pageable.getPageSize());
        int endPage = Math.min(boardDTOPage.getTotalPages(), boardDTOPage.getPageable().getPageNumber() + pageable.getPageSize());
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("pageable",boardDTOPage.getPageable());
        model.addAttribute("page",boardDTOPage);
        model.addAttribute("boardList",boardDTOPage.getContent());
        model.addAttribute("totalElements",totalElements);
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
        BoardDTO board = boardService.viewBoard(boardSn);
        model.addAttribute("view",board);
        model.addAttribute("comment",new BoardComment());
        return "view/boardView";
    }
    @GetMapping("/board/user/forum/edit/{boardSn}")
    public String boardEditForm(@PathVariable("boardSn") Long boardSn, Model model, @AuthenticationPrincipal User user) {
        if(user == null) {
            log.info("사용자 세션 오류입니다");
            return "redirect:/board/user/login";
        }
        BoardDTO board = boardService.viewBoard(boardSn);
        model.addAttribute("view",board);
        return "view/boardEditForm";
    }

    @ResponseBody
    @DeleteMapping("/board/user/forum/{boardSn}")
    public ResponseEntity<String> boardDelete(HttpServletRequest request,
                                              @PathVariable("boardSn") Long boardSn, @AuthenticationPrincipal User user) {
        String memberId = user.getUsername();

        if(user == null) {
            log.info("사용자 세션 오류입니다");
            String message = messageSource.getMessage("error.request.common", null, Locale.getDefault());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
        }
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardSn(boardSn);
        boardDTO.setUpdateDate(LocalDateTime.now());
        boardDTO.setUpdateIp(request.getRemoteAddr());
        boardDTO.setMemberId(memberId);
        String message = messageSource.getMessage("delete.success.common", null, Locale.getDefault());
        boardService.deleteBoard(boardDTO);

        return ResponseEntity.ok(message);
    }

    @PostMapping("/board/user/forum/edit/{boardSn}")
    public String boardUpdate(@Valid BoardDTO boardDTO, Errors errors, MultipartHttpServletRequest request,
                              @PathVariable("boardSn") Long boardSn, Principal principal, Model model) {
        if(errors.hasErrors()){
            model.addAttribute("view",boardDTO);
            return "view/boardEditForm";
        }

        String memberId = principal.getName();

        if(memberId == null) {
            log.info("사용자 세션 오류입니다");
            return "redirect:/board/user/login";
        }
        boardDTO.setBoardSn(boardSn);
        boardDTO.setUpdateDate(LocalDateTime.now());
        boardDTO.setUpdateIp(request.getRemoteAddr());
        boardDTO.setMemberId(memberId);
        boardService.updateBoard(boardDTO,request);

        return "redirect:/board/user/forum/"+boardSn;
    }

    @GetMapping("/board/user/forum/download")
    public void attachFileDownload( HttpServletResponse response,
                                    @RequestParam("attachFileSn") Long attachFileSn) {

        AttachFile attachFile = boardService.findAttachFile(attachFileSn);

        String fileDownNm = attachFile.getSavedPath() + "/" + attachFile.getStoredFileName().trim();
        File file = new File(fileDownNm);

        if(!file.exists()){
            throw new IllegalStateException("파일이 존재하지 않습니다.");
        }

        try(BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
                BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());){

        String realFileName = new String(attachFile.getRealFileName().getBytes("UTF-8"), "8859_1");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\""+(realFileName)+"\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        response.setContentLength((int)file.length());

            IOUtils.copy(fin,outs);
            response.flushBuffer();
        }catch (IOException e) {
            log.info("파일다운로드에 실패했습니다.",e);
        }
    }
}

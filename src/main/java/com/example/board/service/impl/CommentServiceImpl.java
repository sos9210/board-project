package com.example.board.service.impl;

import com.example.board.domain.Board;
import com.example.board.domain.BoardComment;
import com.example.board.domain.Member;
import com.example.board.dto.BoardCommentDTO;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.MemberRepository;
import com.example.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    @Override
    public Long writeComment(BoardCommentDTO boardComment) {
        Member findMember = memberRepository.findById(boardComment.getMemberId()).orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 회원 입니다."));
        Board findBoard = boardRepository.findById(boardComment.getBoardSn()).orElseThrow(() -> new NoSuchElementException("찾을 수 없는 게시물 입니다."));
        BoardComment comment = new BoardComment(boardComment.getContent(), findMember,findBoard,"N", LocalDateTime.now(),boardComment.getRegistIp());

        return commentRepository.save(comment).getBoardCommentSn();
    }

    @Override
    public Page<BoardCommentDTO> commentList(BoardCommentDTO commentDTO, Pageable pageable) {
        return commentRepository.findByCommentList(commentDTO,pageable);
    }

    @Override
    public Long editComment(BoardCommentDTO commentDTO) {
        BoardComment findBoardComment = commentRepository.findByBoardCommentSnAndBoardBoardSn(commentDTO.getBoardCommentSn(), commentDTO.getBoardSn()).orElseThrow(
                () -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다.")
        );
        BoardComment boardComment = findBoardComment;
        boardComment.editComment(commentDTO);
        return boardComment.getBoardCommentSn();
    }

    @Override
    public void deleteComment(BoardCommentDTO commentDTO) {
        BoardComment findBoardComment = commentRepository.findByBoardCommentSnAndBoardBoardSn(commentDTO.getBoardCommentSn(), commentDTO.getBoardSn()).orElseThrow(
                () -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다.")
        );
        BoardComment boardComment = findBoardComment;
        boardComment.deleteComment(commentDTO);
    }


}

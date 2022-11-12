package com.example.board.service.impl;

import com.example.board.domain.AttachFile;
import com.example.board.domain.Board;
import com.example.board.domain.Member;
import com.example.board.dto.BoardCommentDTO;
import com.example.board.dto.BoardDTO;
import com.example.board.repository.AttachFileRepository;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.MemberRepository;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional @Slf4j
public class BoardServiceImpl implements BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final AttachFileRepository attachFileRepository;
    @Override
    public BoardDTO viewBoard(Long boardSn) {
        Board findBoard = boardRepository.findByBoardSnAndDeleteYn(boardSn,"N").orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다."));
        Optional<AttachFile> attachFile = attachFileRepository.findByBoard(findBoard);
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardSn(findBoard.getBoardSn());
        boardDTO.setSubject(findBoard.getSubject());
        boardDTO.setContent(findBoard.getContent());
        boardDTO.setMemberId(findBoard.getMember().getMemberId());
        boardDTO.setAttachFile(attachFile.isEmpty() ? null : attachFile.get());

        return boardDTO;
    }


    @Override
    public Long writeBoard(Board board, MultipartHttpServletRequest request) throws IOException {


        Board saveBoard = boardRepository.save(board);
        try{
            List<AttachFile> attachFiles = fileSave(saveBoard, request);
            for (AttachFile files  :  attachFiles) {
                attachFileRepository.save(files);
            }
        }catch (IOException e){
            log.info("fileUploadFail :: ",e);
            throw e;
        }
        return saveBoard.getBoardSn();
    }

    @Override
    public void deleteBoard(BoardDTO boardDTO) {
        Optional<Member> member = memberRepository.findById(boardDTO.getMemberId());
        Optional<Board> findBoard = boardRepository.findByBoardSnAndMember(boardDTO.getBoardSn(),member.get());
        if(findBoard.isEmpty()){
            throw new NoSuchElementException("해당 게시물을 찾을 수 없습니다.");
        }
        Board board = findBoard.get();
        board.boardDelete();
    }

    @Override
    public Long updateBoard(BoardDTO boardDTO, MultipartHttpServletRequest request) {
        Optional<Member> member = memberRepository.findById(boardDTO.getMemberId());
        Board findBoard = boardRepository.findByBoardSnAndMember(boardDTO.getBoardSn(),member.get())
                .orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다."));


        try{
            List<AttachFile> attachFiles = fileSave(findBoard, request);
            if(attachFiles.size() > 0){
                //수정시 등록된 파일이 존재하면 기존 파일을 삭제하고 새로 등록한다.
                attachFileRepository.deleteByBoard(findBoard);
                for (AttachFile files  :  attachFiles) {
                    attachFileRepository.save(files);
                }
            }
        }catch (IOException e){
            log.error("fileUploadFail :: ",e);
        }
        findBoard.boardUpdate(boardDTO);

        return findBoard.getBoardSn();
    }


    @Override
    public Page<BoardDTO> listForum(BoardDTO search, Pageable pageable) {
        return boardRepository.findByBoardList(search,pageable);
    }

    private List<AttachFile> fileSave(Board board, MultipartHttpServletRequest request) throws IOException {
        Iterator<String> itr =  request.getFileNames();
        List<AttachFile> attachFileList = new ArrayList<>();

        while(itr.hasNext()) {
            String nextFile = itr.next();
            MultipartFile multipartFile = request.getFile(nextFile);
            if(!StringUtils.hasText(multipartFile.getOriginalFilename())){
                return attachFileList;
            }
            String[] originalFilenameAndExt = multipartFile.getOriginalFilename().split("\\.");
            String rootPath = System.getProperty("user.dir");

            long fileSize = multipartFile.getSize();                                    //파일사이즈
            String extension = originalFilenameAndExt[1];                               //확장자
            String originalFileName = originalFilenameAndExt[0]+"."+extension;          //실제파일명
            String storedFileName = String.valueOf(UUID.randomUUID())+"."+extension;    //저장파일명
            String uploadPath = rootPath+"/upload/"+board.getBoardSn()+"/";             //업로드경로
            File file = new File(uploadPath);
            if(!file.exists()){
                file.mkdir();
            }

            multipartFile.transferTo(new File(uploadPath+storedFileName));     //파일생성

            attachFileList.add(new AttachFile(originalFileName,storedFileName,extension,fileSize,uploadPath,board, LocalDateTime.now(),request.getRemoteAddr()));
        }
        return attachFileList;
    }

    @Override
    public AttachFile findAttachFile(Long attachFileSn) {
        AttachFile findFile = attachFileRepository.findById(attachFileSn).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 파일정보 입니다."));
        return findFile;
    }
}

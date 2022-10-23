package com.example.board.service.impl;

import com.example.board.domain.AttachFile;
import com.example.board.domain.Board;
import com.example.board.repository.AttachFileRepository;
import com.example.board.repository.BoardRepository;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private final BoardRepository boardRepository;
    private final AttachFileRepository attachFileRepository;


    @Override
    public Board viewBoard(Long boardSn) {
        Board findBoard = boardRepository.findById(boardSn).orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다."));
        return findBoard;
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

    private List<AttachFile> fileSave(Board board, MultipartHttpServletRequest request) throws IOException {
        Iterator<String> itr =  request.getFileNames();
        List<AttachFile> attachFileList = new ArrayList<>();

        while(itr.hasNext()) {
            MultipartFile multipartFile = request.getFile(itr.next());
            String[] originalFilenameAndExt = multipartFile.getOriginalFilename().split("\\.");
            String rootPath = System.getProperty("user.dir");

            long fileSize = multipartFile.getSize();                                    //파일사이즈
            String originalFileName = originalFilenameAndExt[0];                        //실제파일명
            String extension = originalFilenameAndExt[1];                               //확장자
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

}

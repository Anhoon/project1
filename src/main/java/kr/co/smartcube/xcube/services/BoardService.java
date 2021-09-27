package kr.co.smartcube.xcube.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import kr.co.smartcube.xcube.mybatis.dao.BoardDao;
import kr.co.smartcube.xcube.util.Util;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardService {

    @Autowired
    private FileService fileService;

    @Autowired
    private BoardDao boardDao;

    /*일반 게시판용*/
    /*자료등록*/
    public PageInfo<Map<String, Object>> selectBoardList(Map<String,Object> paramMap) throws Exception{
        List<Map<String,Object>> fileList = null;
        PageInfo<Map<String, Object>> boardPageInfoList = boardDao.selectBoardList(paramMap);

        if(!ObjectUtils.isEmpty(boardPageInfoList)){
            for(Map<String, Object> boardMap : boardPageInfoList.getList()){
                fileList = fileService.selectFileList(boardMap);
                if(!ObjectUtils.isEmpty(fileList)) boardMap.put("file", fileList);
            }
        }
        
        return boardPageInfoList;
    }

    public Map<String, Object> selectBoard(Map<String,Object> paramMap) throws Exception{
        Map<String, Object> boardMap = boardDao.selectBoard(paramMap);

        if(ObjectUtils.isEmpty(boardMap))
            throw new RuntimeException("일치하는 정보가 없습니다.");

        List<Map<String,Object>> fileList = fileService.selectFileList(boardMap);

        if(!ObjectUtils.isEmpty(fileList))
            boardMap.put("file", fileList);
        
        return boardMap;
    }

    @Transactional
    public void insertBoard(Map<String, Object> paramMap) throws Exception{
        List<Map<String,Object>> fileInsertList = new ArrayList<Map<String,Object>>();
        Map<String,Object> fileGroupInfoMap = new HashMap<String,Object>();
        List<Map<String,Object>> pfileList = Util.objToList(paramMap.get("file"));
        Map<String,Object> pBoardMap = Util.objToMap(paramMap.get("board"));
        
        fileGroupInfoMap.put("fileGroup", paramMap.get("fileGroup"));

        if(!ObjectUtils.isEmpty(pfileList)){
            for(Map<String,Object> pfileMap : pfileList){
                fileGroupInfoMap.put("fileSubGroup", pfileMap.get("fileSubGroup"));
                if(!ObjectUtils.isEmpty(pfileMap.get("fileName")) && !ObjectUtils.isEmpty(pfileMap.get("fileContent")))
                    fileInsertList.add(fileService.fileUploadByte(pfileMap.get("fileName").toString(), pfileMap.get("fileContent").toString(), fileGroupInfoMap));
            }
        }

        pBoardMap.put("attachObid", Util.getAttachObid(fileInsertList));
        pBoardMap.put("obid", UUID.randomUUID().toString());
        pBoardMap.put("boardType", paramMap.get("boardType"));

        boardDao.insertBoard(pBoardMap);
        fileService.insertFileList(fileInsertList);
    }

    @Transactional
    public void updateBoard(Map<String, Object> paramMap) throws Exception{
        Map<String, Object> boardMap = boardDao.selectBoard(paramMap);
        if(ObjectUtils.isEmpty(boardMap))
            throw new RuntimeException("일치하는 정보가 없습니다.");

        List<Map<String,Object>> fileInsertList = new ArrayList<Map<String,Object>>();
        Map<String,Object> fileGroupInfoMap = new HashMap<String,Object>();
        List<Map<String,Object>> pfileList = Util.objToList(paramMap.get("file"));
        Map<String,Object> pboardMap = Util.objToMap(paramMap.get("board"));
        ArrayList<Object> attachFile = new ArrayList<Object>();
        
        fileGroupInfoMap.put("fileGroup", paramMap.get("fileGroup"));

        if(!ObjectUtils.isEmpty(pfileList)){
            for(Map<String,Object> pfileMap : pfileList){
                if(!ObjectUtils.isEmpty(pfileMap.get("fileName")) && !ObjectUtils.isEmpty(pfileMap.get("filePath")) && !ObjectUtils.isEmpty(pfileMap.get("obid"))){
                    attachFile = Util.jsonToArray(boardMap.get("attachObid").toString());
                    attachFile.remove(pfileMap.get("obid"));
                    fileService.deleteFile(pfileMap);
                }
                    
                fileGroupInfoMap.put("fileSubGroup", pfileMap.get("fileSubGroup"));
                if(!ObjectUtils.isEmpty(pfileMap.get("fileName")) && !ObjectUtils.isEmpty(pfileMap.get("fileContent")))
                    fileInsertList.add(fileService.fileUploadByte(pfileMap.get("fileName").toString(), pfileMap.get("fileContent").toString(), fileGroupInfoMap));
            }
        }

        pboardMap.put("attachObid", Util.getAttachObid(attachFile, fileInsertList));
        pboardMap.put("obid", paramMap.get("obid"));
        pboardMap.put("boardType", paramMap.get("boardType"));

        boardDao.updateBoard(pboardMap);
        fileService.insertFileList(fileInsertList);
    }

    @Transactional
    public void deleteBoard(Map<String, Object> paramMap) throws Exception{
        Map<String, Object> boardMap = boardDao.selectBoard(paramMap);
        if(ObjectUtils.isEmpty(boardMap)) throw new RuntimeException("일치하는 정보가 없습니다.");
        if(!ObjectUtils.isEmpty(boardMap.get("attachObid"))) fileService.deleteFileList(fileService.selectFileList(boardMap));
        boardDao.deleteBoard(paramMap);
    }

    @Transactional
    public void deleteBoardList(Map<String, Object> paramMap) throws Exception{
        Map<String, Object> boardMap = null;
        Map<String, Object> map = new HashMap<String,Object>();
        ArrayList<String> arrayList = (ArrayList<String>) paramMap.get("obid");

        for(String obid : arrayList){
            if(!ObjectUtils.isEmpty(arrayList)){
                map.put("obid", obid);
                boardMap = boardDao.selectBoard(map);
                if(!ObjectUtils.isEmpty(boardMap) && !ObjectUtils.isEmpty(boardMap.get("attachObid"))){
                    fileService.deleteFileList(fileService.selectFileList(boardMap));
                }
            }
        }

        boardDao.deleteBoardList(paramMap);
    }
}

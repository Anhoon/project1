package kr.co.smartcube.xcube.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import kr.co.smartcube.xcube.mybatis.dao.FileDao;
import kr.co.smartcube.xcube.mybatis.dao.FileManageDao;
import kr.co.smartcube.xcube.util.Util;

@Service
public class FileManageService {

    @Autowired
    private FileManageDao fileManageDao;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileDao fileDao;


    /*파일관리*/
    public PageInfo<Map<String, Object>> selectFileManageList(Map<String,Object> paramMap) throws Exception {
        PageHelper.startPage((int)paramMap.get("pageNum"), (int)paramMap.get("pageSize"), (String) paramMap.get("orderBy"));
        return fileManageDao.selectFileManageList(paramMap);
    }

    public Map<String, Object> selectFileManage(Map<String,Object> paramMap) throws Exception {
        validateFileManage(paramMap, "S");
        
        Map<String, Object> fileParamMap = new HashMap<String, Object>();
        Map<String, Object> resultMap = fileManageDao.selectFileManage(paramMap);
        if(ObjectUtils.isEmpty(resultMap)) throw new RuntimeException("일치하는 정보가 없습니다.");
        
        fileParamMap.put("refObid", resultMap.get("attatchFileObid"));
        List<Map<String,Object>> fileList = fileService.selectFileList(fileParamMap);
        resultMap.put("attatchFiles", fileList);

        return resultMap;
    }

    @Transactional
    public void insertFileManage(Map<String, Object> paramMap) throws Exception{
        validateFileManage(paramMap, "I");

        Map<String,Object> fileMap = new HashMap<String,Object>();
        Map<String,Object> fileGroupMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> fileParamList = Util.objToList(paramMap.get("attatchFiles"));

        fileGroupMap.put("fileGroup", "file");
        fileGroupMap.put("fileSubGroup", "manage");
        paramMap.put("obid", UUID.randomUUID().toString());
        
        if(!ObjectUtils.isEmpty(fileParamList)){
            paramMap.put("attatchFileObid", UUID.randomUUID().toString());

            for(Map<String,Object> fileParamMap:fileParamList){
                fileMap = fileService.fileUploadByte(fileParamMap.get("fileName").toString(), fileParamMap.get("fileContent").toString(), paramMap.get("attatchFileObid").toString(), fileGroupMap);
                if(!ObjectUtils.isEmpty(fileMap)){
                    fileList.add(fileMap);
                }
            }

            if(!ObjectUtils.isEmpty(fileList)) fileDao.insertFileList(fileList);
        }
        
        fileManageDao.insertFileManage(paramMap);
    }

    @Transactional
    public void updateFileManage(Map<String, Object> paramMap) throws Exception{
        validateFileManage(paramMap, "U");

        Map<String,Object> map = fileManageDao.selectFileManage(paramMap);

        if(ObjectUtils.isEmpty(map)) 
            throw new RuntimeException("일치하는 정보가 없습니다."); 

        Map<String,Object> fileGroupMap = new HashMap<String,Object>();
        List<Map<String,Object>> insertFileList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> pFileList = Util.objToList(paramMap.get("attatchFiles"));

        fileGroupMap.put("fileGroup", "file");
        fileGroupMap.put("fileSubGroup", "manage");

        if(!ObjectUtils.isEmpty(pFileList)){
            if(ObjectUtils.isEmpty(map.get("attatchFileObid"))) map.put("attatchFileObid", UUID.randomUUID().toString());

            for(Map<String,Object> pfileMap : pFileList){
                if(!ObjectUtils.isEmpty(pfileMap) && !ObjectUtils.isEmpty(pfileMap.get("fileName")) && !ObjectUtils.isEmpty(pfileMap.get("filePath")) && !ObjectUtils.isEmpty(pfileMap.get("obid"))) 
                    fileService.deleteFile(pfileMap);

                if(!ObjectUtils.isEmpty(pfileMap.get("fileName")) && !ObjectUtils.isEmpty(pfileMap.get("fileContent")))
                    insertFileList.add(fileService.fileUploadByte(pfileMap.get("fileName").toString(), pfileMap.get("fileContent").toString(), map.get("attatchFileObid").toString(), fileGroupMap));
            }

            if(!ObjectUtils.isEmpty(insertFileList)) fileDao.insertFileList(insertFileList);
        }

        paramMap.put("attatchFileObid", map.get("attatchFileObid"));
        fileManageDao.updateFileManage(paramMap);
    }

    @Transactional
    public void deleteFileManage(Map<String, Object> paramMap) throws Exception{
        validateFileManage(paramMap, "D");
        Map<String, Object> map = null;
        Map<String,Object> fileParamMap = null;
        
        if(!ObjectUtils.isEmpty(paramMap) && !ObjectUtils.isEmpty(paramMap.get("obid")))
        {
            map = fileManageDao.selectFileManage(paramMap);
            fileParamMap = new HashMap<String, Object>();
            fileParamMap.put("refObid", map.get("attatchFileObid"));
            fileService.deleteFileList(fileParamMap);
        }

        fileManageDao.deleteFileManage(paramMap);
    }

    @Transactional
    public void deleteFileManageList(Map<String, Object> paramMap) throws Exception{
        validateFileManage(paramMap, "DL");
        Map<String, Object> map = null;
        Map<String,Object> fileParamMap = null;

        List<Map<String,Object>> authList = Util.objToList(paramMap.get("list"));
        
        for(Map<String,Object> authMap : authList){
            if(!ObjectUtils.isEmpty(authMap) && !ObjectUtils.isEmpty(authMap.get("obid")))
            {
                map = fileManageDao.selectFileManage(authMap);
                fileParamMap = new HashMap<String, Object>();
                fileParamMap.put("refObid", map.get("attatchFileObid"));
                fileService.deleteFileList(fileParamMap);
            }
        }

        fileManageDao.deleteFileManageList(authList);
    }

    public void validateFileManage(Map<String, Object> paramMap, String type){
        if(ObjectUtils.isEmpty(paramMap)) throw new RuntimeException("데이터를 입력하세요.");
        if(type.equals("I")){
            if(ObjectUtils.isEmpty(paramMap.get("masterLicenseListObid")))  throw new RuntimeException("행사를 선택하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("fileName"))) throw new RuntimeException("파일명을 입력하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("fileType"))) throw new RuntimeException("파일분류를 선택하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("fileDescript"))) throw new RuntimeException("파일설명을 입력하세요.");
        }
        if(type.equals("U")){
            if(ObjectUtils.isEmpty(paramMap.get("obid")))  throw new RuntimeException("선택된 파일관리정보가 없습니다.");
            if(ObjectUtils.isEmpty(paramMap.get("masterLicenseListObid")))  throw new RuntimeException("행사를 선택하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("fileName"))) throw new RuntimeException("파일명을 입력하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("fileType"))) throw new RuntimeException("파일분류를 선택하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("fileDescript"))) throw new RuntimeException("파일설명을 입력하세요.");
        }
        if(type.equals("S") || type.equals("D")){
            if(ObjectUtils.isEmpty(paramMap.get("obid")))  throw new RuntimeException("선택된 파일관리정보가 없습니다.");
        }
        if(type.equals("DL") || type.equals("UL")){
            if(ObjectUtils.isEmpty(paramMap.get("list"))) throw new RuntimeException("대상을 선택하시기 바랍니다.");
        }
    }
}
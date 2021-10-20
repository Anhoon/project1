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
import kr.co.smartcube.xcube.mybatis.dao.Req3dDao;
import kr.co.smartcube.xcube.util.Util;

@Service
public class Req3dService {

    @Autowired
    private Req3dDao req3dDao;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileDao fileDao;


    /*3D제작요청관리*/
    public PageInfo<Map<String, Object>> selectReq3dList(Map<String,Object> paramMap) throws Exception {
        PageHelper.startPage((int)paramMap.get("pageNum"), (int)paramMap.get("pageSize"), (String) paramMap.get("orderBy"));
        return req3dDao.selectReq3dList(paramMap);
    }

    public Map<String, Object> selectReq3d(Map<String,Object> paramMap) throws Exception {
        validateReq3d(paramMap, "S");
        
        Map<String, Object> fileParamMap = new HashMap<String, Object>();
        Map<String, Object> resultMap = req3dDao.selectReq3d(paramMap);
        if(ObjectUtils.isEmpty(resultMap)) throw new RuntimeException("일치하는 정보가 없습니다.");
        
        fileParamMap.put("refObid", resultMap.get("attatchFileObid"));
        List<Map<String,Object>> fileList = fileService.selectFileList(fileParamMap);
        resultMap.put("attatchFiles", fileList);

        fileParamMap.put("refObid", resultMap.get("imageFileObid"));
        List<Map<String,Object>> imgFileList = fileService.selectFileList(fileParamMap);
        resultMap.put("imageFiles", imgFileList);

        return resultMap;
    }

    @Transactional
    public void insertReq3d(Map<String, Object> paramMap) throws Exception{
        validateReq3d(paramMap, "I");

        Map<String,Object> fileMap = new HashMap<String,Object>();
        Map<String,Object> fileGroupMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> imgFileList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> fileParamList = Util.objToList(paramMap.get("attatchFiles"));
        List<Map<String,Object>> imgFileParamList = Util.objToList(paramMap.get("imageFiles"));

        fileGroupMap.put("fileGroup", "req3d");
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

        if(!ObjectUtils.isEmpty(imgFileParamList)){
            paramMap.put("imageFileObid", UUID.randomUUID().toString());

            for(Map<String,Object> imgFileParamMap:imgFileParamList){
                fileMap = fileService.fileUploadByte(imgFileParamMap.get("fileName").toString(), imgFileParamMap.get("fileContent").toString(), paramMap.get("imageFileObid").toString(), fileGroupMap);
                if(!ObjectUtils.isEmpty(fileMap)){
                    imgFileList.add(fileMap);
                }
            }

            if(!ObjectUtils.isEmpty(imgFileList)) fileDao.insertFileList(imgFileList);
        }
        
        req3dDao.insertReq3d(paramMap);
    }

    @Transactional
    public void updateReq3d(Map<String, Object> paramMap) throws Exception{
        validateReq3d(paramMap, "U");

        Map<String,Object> map = req3dDao.selectReq3d(paramMap);

        if(ObjectUtils.isEmpty(map)) 
            throw new RuntimeException("일치하는 정보가 없습니다."); 

        Map<String,Object> fileGroupMap = new HashMap<String,Object>();
        List<Map<String,Object>> insertFileList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> insertImgFileList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> pFileList = Util.objToList(paramMap.get("attatchFiles"));
        List<Map<String,Object>> pImgFileList = Util.objToList(paramMap.get("imageFiles"));

        fileGroupMap.put("fileGroup", "req3d");

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

        if(!ObjectUtils.isEmpty(pImgFileList)){
            if(ObjectUtils.isEmpty(map.get("imageFileObid"))) map.put("imageFileObid", UUID.randomUUID().toString());

            for(Map<String,Object> pImgfileMap : pImgFileList){
                if(!ObjectUtils.isEmpty(pImgfileMap) && !ObjectUtils.isEmpty(pImgfileMap.get("fileName")) && !ObjectUtils.isEmpty(pImgfileMap.get("filePath")) && !ObjectUtils.isEmpty(pImgfileMap.get("obid"))) 
                    fileService.deleteFile(pImgfileMap);

                if(!ObjectUtils.isEmpty(pImgfileMap.get("fileName")) && !ObjectUtils.isEmpty(pImgfileMap.get("fileContent")))
                    insertImgFileList.add(fileService.fileUploadByte(pImgfileMap.get("fileName").toString(), pImgfileMap.get("fileContent").toString(), map.get("imageFileObid").toString(), fileGroupMap));
            }

            if(!ObjectUtils.isEmpty(insertImgFileList)) fileDao.insertFileList(insertImgFileList);
        }

        paramMap.put("attatchFileObid", map.get("attatchFileObid"));
        paramMap.put("imageFileObid", map.get("imageFileObid"));
        req3dDao.updateReq3d(paramMap);
    }

    @Transactional
    public void updateReq3dStatus(Map<String, Object> paramMap) throws Exception{
        validateReq3d(paramMap, "US");
        if(ObjectUtils.isEmpty(req3dDao.selectReq3d(paramMap))) throw new RuntimeException("일치하는 정보가 없습니다."); 
        req3dDao.updateReq3dStatus(paramMap);
    }

    @Transactional
    public void updateReq3dDone(Map<String, Object> paramMap) throws Exception{
        validateReq3d(paramMap, "UD");
        if(ObjectUtils.isEmpty(req3dDao.selectReq3d(paramMap))) throw new RuntimeException("일치하는 정보가 없습니다."); 
        req3dDao.updateReq3dDone(paramMap);
    }

    @Transactional
    public void deleteReq3d(Map<String, Object> paramMap) throws Exception{
        validateReq3d(paramMap, "D");
        Map<String, Object> map = null;
        Map<String,Object> fileParamMap = null;
        
        if(!ObjectUtils.isEmpty(paramMap) && !ObjectUtils.isEmpty(paramMap.get("obid")))
        {
            map = req3dDao.selectReq3d(paramMap);
            fileParamMap = new HashMap<String, Object>();
            fileParamMap.put("refObid", map.get("attatchFileObid"));
            fileService.deleteFileList(fileParamMap);
            fileParamMap.put("refObid", map.get("imageFileObid"));
            fileService.deleteFileList(fileParamMap);
        }

        req3dDao.deleteReq3d(paramMap);
    }

    @Transactional
    public void deleteReq3dList(Map<String, Object> paramMap) throws Exception{
        validateReq3d(paramMap, "DL");
        Map<String, Object> fileMap = null;
        Map<String,Object> fileParamMap = null;

        List<Map<String,Object>> paramList = Util.objToList(paramMap.get("list"));
        if(ObjectUtils.isEmpty(paramList)) throw new RuntimeException("대상이 없습니다."); 
        
        for(Map<String,Object> map : paramList){
            if(!ObjectUtils.isEmpty(map) && !ObjectUtils.isEmpty(map.get("obid")))
            {
                fileMap = req3dDao.selectReq3d(map);
                fileParamMap = new HashMap<String, Object>();
                fileParamMap.put("refObid", fileMap.get("attatchFileObid"));
                fileService.deleteFileList(fileParamMap);
                fileParamMap.put("refObid", fileMap.get("imageFileObid"));
                fileService.deleteFileList(fileParamMap);
            }
        }

        req3dDao.deleteReq3dList(paramList);
    }

    public void validateReq3d(Map<String, Object> paramMap, String type){
        if(ObjectUtils.isEmpty(paramMap)) throw new RuntimeException("데이터를 입력하세요.");
        if(type.equals("I")){
            if(ObjectUtils.isEmpty(paramMap.get("masterLicenseListObid")))  throw new RuntimeException("행사를 선택하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("email")))  throw new RuntimeException("이메일을 선택하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("fileName"))) throw new RuntimeException("파일명을 입력하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("fileType"))) throw new RuntimeException("파일분류를 선택하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("fileDescript"))) throw new RuntimeException("파일설명을 입력하세요.");
        }
        if(type.equals("U")){
            if(ObjectUtils.isEmpty(paramMap.get("obid")))  throw new RuntimeException("선택된 3D제작요청이 없습니다.");
            if(ObjectUtils.isEmpty(paramMap.get("masterLicenseListObid")))  throw new RuntimeException("행사를 선택하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("email")))  throw new RuntimeException("이메일을 선택하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("fileName"))) throw new RuntimeException("파일명을 입력하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("fileType"))) throw new RuntimeException("파일분류를 선택하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("fileDescript"))) throw new RuntimeException("파일설명을 입력하세요.");
        }
        if(type.equals("US")){
            if(ObjectUtils.isEmpty(paramMap.get("obid")))  throw new RuntimeException("선택된 3D제작요청이 없습니다.");
            if(ObjectUtils.isEmpty(paramMap.get("reqStatus"))) throw new RuntimeException("요청상태를 선택하세요.");
        }
        if(type.equals("UD")){
            if(ObjectUtils.isEmpty(paramMap.get("obid")))  throw new RuntimeException("선택된 3D제작요청이 없습니다.");
            if(ObjectUtils.isEmpty(paramMap.get("done"))) throw new RuntimeException("제작완료여부를 선택하세요.");
        }
        if(type.equals("S") || type.equals("D")){
            if(ObjectUtils.isEmpty(paramMap.get("obid")))  throw new RuntimeException("선택된 3D제작요청이 없습니다.");
        }
        if(type.equals("DL") || type.equals("UL")){
            if(ObjectUtils.isEmpty(paramMap.get("list"))) throw new RuntimeException("대상을 선택하시기 바랍니다.");
        }
    }
}
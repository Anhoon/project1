package kr.co.smartcube.xcube.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import kr.co.smartcube.xcube.mybatis.dao.FileDao;
import kr.co.smartcube.xcube.mybatis.dao.ManageDao;
import kr.co.smartcube.xcube.util.Util;

@Service
public class ManageService {

    @Autowired
    private ManageDao manageDao;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileDao fileDao;

    /*참가기업*/
    public PageInfo<List<Map<String, Object>>> selectJoinCompanyList(Map<String,Object> paramMap) throws Exception {
        PageHelper.startPage((int)paramMap.get("pageNum"), (int)paramMap.get("pageSize"), (String) paramMap.get("orderBy"));
        return manageDao.selectJoinCompanyList(paramMap);
    }

    public Map<String, Object> selectJoinCompany(Map<String,Object> paramMap) throws Exception {
        Map<String,Object> returnMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = null;
        Map<String,Object> joinCompanyMap = manageDao.selectJoinCompany(paramMap);

        if(ObjectUtils.isEmpty(joinCompanyMap)) throw new RuntimeException("일치하는 정보가 없습니다.");
        if(!ObjectUtils.isEmpty(joinCompanyMap.get("attachObid"))){
            fileList = fileService.selectFileList(joinCompanyMap);
            returnMap.put("file", fileList);
        }

        returnMap.put("company", joinCompanyMap);

        return returnMap;
    }

    @Transactional
    public void insertJoinCompany(Map<String, Object> paramMap) throws Exception{
        Map<String,Object> fileMap = new HashMap<String,Object>();
        Map<String,Object> fileGroupMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        Map<String,Object> paramFileMap = Util.objToMap(paramMap.get("files"));

        fileGroupMap.put("fileGroup", "manage");
        fileGroupMap.put("fileSubGroup", "joinCompany");

        if(!ObjectUtils.isEmpty(paramFileMap)){
            fileMap = fileService.fileUploadByte(Util.objToStr(paramFileMap.get("fileName")), Util.objToStr(paramFileMap.get("fileContent")), fileGroupMap);
            paramMap.put("attatchObid", fileMap.get("obid"));
            fileList.add(fileMap);
        }

        manageDao.insertJoinCompany(paramMap);
        fileDao.insertFile(fileList);
    } 

    @Transactional
    public void updateJoinCompany(Map<String, Object> paramMap) throws Exception{
        Map<String,Object> joinCompanyMap = manageDao.selectJoinCompany(paramMap);
        if(ObjectUtils.isEmpty(joinCompanyMap)) throw new RuntimeException("일치하는 정보가 없습니다."); 

        Map<String,Object> fileMap = new HashMap<String,Object>();
        Map<String,Object> fileGroupMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        Map<String,Object> paramFileMap = Util.objToMap(paramMap.get("files"));

        fileGroupMap.put("fileGroup", "manage");
        fileGroupMap.put("fileSubGroup", "joinCompany");

        if(!Util.objToStr(joinCompanyMap.get("fileName")).equals(Util.objToStr(paramFileMap.get("fileName")))){
            fileMap = fileService.fileUploadByte(Util.objToStr(paramFileMap.get("fileName")), Util.objToStr(paramFileMap.get("fileContent")), fileGroupMap);
            paramMap.put("attatchObid", fileMap.get("obid"));
            fileList.add(fileMap);
            fileService.deleteFile(joinCompanyMap);
        }else{
            paramMap.put("attatchObid", joinCompanyMap.get("attatchObid"));
        }
        
        manageDao.updateJoinCompany(paramMap);
        fileDao.insertFile(fileList);
    }

    @Transactional
    public void deleteJoinCompany(Map<String, Object> paramMap) throws Exception{
        Map<String,Object> joinCompanyMap = manageDao.selectJoinCompany(paramMap);
        if(ObjectUtils.isEmpty(joinCompanyMap)) throw new RuntimeException("일치하는 정보가 없습니다.");
        
        Map<String,Object> fileMap = fileService.selectFile(joinCompanyMap);
        if(!ObjectUtils.isEmpty(fileMap))fileService.deleteFile(fileMap);

        manageDao.deleteJoinCompany(joinCompanyMap);
    }

    /*참여인력*/
    public PageInfo<List<Map<String, Object>>> selectJoinUserList(Map<String,Object> paramMap) throws Exception {
        PageHelper.startPage((int)paramMap.get("pageNum"), (int)paramMap.get("pageSize"), (String) paramMap.get("orderBy"));
        return manageDao.selectJoinUserList(paramMap);
    }

    public Map<String, Object> selectJoinUser(Map<String,Object> paramMap) throws Exception {
        Map<String,Object> returnMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = null;
        Map<String,Object> joinUserMap = manageDao.selectJoinUser(paramMap);

        if(ObjectUtils.isEmpty(joinUserMap)) throw new RuntimeException("일치하는 정보가 없습니다.");
        if(!ObjectUtils.isEmpty(joinUserMap.get("attachObid"))){
            fileList = fileService.selectFileList(joinUserMap);
            returnMap.put("file", fileList);
        }

        returnMap.put("user", joinUserMap);

        return returnMap;
    }

    @Transactional
    public void insertJoinUser(Map<String, Object> paramMap) throws Exception{
        Map<String,Object> fileMap = new HashMap<String,Object>();
        Map<String,Object> fileGroupMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        Map<String,Object> paramFileMap = Util.objToMap(paramMap.get("files"));

        fileGroupMap.put("fileGroup", "manage");
        fileGroupMap.put("fileSubGroup", "joinUser");

        if(!ObjectUtils.isEmpty(paramFileMap)){
            fileMap = fileService.fileUploadByte(Util.objToStr(paramFileMap.get("fileName")), Util.objToStr(paramFileMap.get("fileContent")), fileGroupMap);
            paramMap.put("attatchObid", fileMap.get("obid"));
            fileList.add(fileMap);
        }

        manageDao.insertJoinUser(paramMap);
        fileDao.insertFile(fileList);
    } 

    @Transactional
    public void updateJoinUser(Map<String, Object> paramMap) throws Exception{
        Map<String,Object> joinUserMap = manageDao.selectJoinCompany(paramMap);
        if(ObjectUtils.isEmpty(joinUserMap)) throw new RuntimeException("일치하는 정보가 없습니다."); 

        Map<String,Object> fileMap = new HashMap<String,Object>();
        Map<String,Object> fileGroupMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        Map<String,Object> paramFileMap = Util.objToMap(paramMap.get("files"));

        fileGroupMap.put("fileGroup", "manage");
        fileGroupMap.put("fileSubGroup", "joinUser");

        if(!Util.objToStr(joinUserMap.get("fileName")).equals(Util.objToStr(paramFileMap.get("fileName")))){
            fileMap = fileService.fileUploadByte(Util.objToStr(paramFileMap.get("fileName")), Util.objToStr(paramFileMap.get("fileContent")), fileGroupMap);
            paramMap.put("attatchObid", fileMap.get("obid"));
            fileList.add(fileMap);
            fileService.deleteFile(joinUserMap);
        }else{
            paramMap.put("attatchObid", joinUserMap.get("attatchObid"));
        }

        manageDao.updateJoinUser(paramMap);
        fileDao.insertFile(fileList);
    }

    @Transactional
    public void deleteJoinUser(Map<String, Object> paramMap) throws Exception{
        Map<String,Object> joinUserMap = manageDao.selectJoinUser(paramMap);
        if(ObjectUtils.isEmpty(joinUserMap)) throw new RuntimeException("일치하는 정보가 없습니다.");

        Map<String,Object> fileMap = fileService.selectFile(joinUserMap);
        if(!ObjectUtils.isEmpty(fileMap)) fileService.deleteFile(fileMap);

        manageDao.deleteJoinCompany(joinUserMap);
    }    
}
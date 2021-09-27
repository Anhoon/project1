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
    public PageInfo<Map<String, Object>> selectJoinCompanyList(Map<String,Object> paramMap) throws Exception {
        List<Map<String,Object>> fileList = null;
        PageHelper.startPage((int)paramMap.get("pageNum"), (int)paramMap.get("pageSize"), (String) paramMap.get("orderBy"));
        PageInfo<Map<String, Object>> joinCompanyList = manageDao.selectJoinCompanyList(paramMap);

        if(!ObjectUtils.isEmpty(joinCompanyList)){
            for(Map<String, Object> joinCompanyMap:joinCompanyList.getList()){
                if(!ObjectUtils.isEmpty(joinCompanyMap) && !ObjectUtils.isEmpty(joinCompanyMap.get("attachObid"))){
                    fileList = fileService.selectFileList(joinCompanyMap);
                    if(!ObjectUtils.isEmpty(fileList)) joinCompanyMap.put("file", fileList);
                }
            }
        }

        return joinCompanyList;
    }

    public Map<String, Object> selectJoinCompany(Map<String,Object> paramMap) throws Exception {
        Map<String,Object> returnMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = null;
        Map<String,Object> joinCompanyMap = manageDao.selectJoinCompany(paramMap);

        if(ObjectUtils.isEmpty(joinCompanyMap)) throw new RuntimeException("일치하는 정보가 없습니다.");
        if(!ObjectUtils.isEmpty(joinCompanyMap.get("test3"))) joinCompanyMap.put("test3", Util.jsonToArray(Util.objToStr(joinCompanyMap.get("test3"))));
        if(!ObjectUtils.isEmpty(joinCompanyMap.get("attachObid"))){
            fileList = fileService.selectFileList(joinCompanyMap);
            joinCompanyMap.put("attachObid", Util.jsonToArray(Util.objToStr(joinCompanyMap.get("attachObid"))));
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
        List<Map<String,Object>> paramFileList = Util.objToList(paramMap.get("file"));

        fileGroupMap.put("fileGroup", "manage");
        fileGroupMap.put("fileSubGroup", "joinCompany");

        if(!ObjectUtils.isEmpty(paramMap.get("test3"))) paramMap.put("test3",Util.objToJson(paramMap.get("test3")));
        if(!ObjectUtils.isEmpty(paramFileList)){
            for(Map<String,Object> paramFileMap:paramFileList){
                fileMap = fileService.fileUploadByte(Util.objToStr(paramFileMap.get("fileName")), Util.objToStr(paramFileMap.get("fileContent")), fileGroupMap);
                if(!ObjectUtils.isEmpty(fileMap)){
                    fileList.add(fileMap);
                    paramMap.put("attachObid", Util.getAttachObid(fileList));
                }
            }
        }

        manageDao.insertJoinCompany(paramMap);
        fileDao.insertFile(fileList);
    } 

    @Transactional
    public void updateJoinCompany(Map<String, Object> paramMap) throws Exception{
        Map<String,Object> joinCompanyMap = manageDao.selectJoinCompany(paramMap);
        if(ObjectUtils.isEmpty(joinCompanyMap)) throw new RuntimeException("일치하는 정보가 없습니다."); 

        Map<String,Object> fileGroupMap = new HashMap<String,Object>();
        List<Map<String,Object>> insertFileList = new ArrayList<Map<String,Object>>();
        ArrayList<Object> attachFile = new ArrayList<Object>();
        List<Map<String,Object>> pFileList = Util.objToList(paramMap.get("file"));

        fileGroupMap.put("fileGroup", "manage");
        fileGroupMap.put("fileSubGroup", "joinCompany");

        if(!ObjectUtils.isEmpty(paramMap.get("test3"))) paramMap.put("test3",Util.objToJson(paramMap.get("test3")));
        if(!ObjectUtils.isEmpty(joinCompanyMap.get("attachObid")))  attachFile = Util.jsonToArray(joinCompanyMap.get("attachObid").toString());
        if(!ObjectUtils.isEmpty(pFileList)){
            for(Map<String,Object> pfileMap : pFileList){
                if(!ObjectUtils.isEmpty(attachFile) && 
                    !ObjectUtils.isEmpty(pfileMap) && 
                    !ObjectUtils.isEmpty(pfileMap.get("fileName")) && 
                    !ObjectUtils.isEmpty(pfileMap.get("filePath")) && 
                    !ObjectUtils.isEmpty(pfileMap.get("obid")))
                {
                    attachFile.remove(pfileMap.get("obid"));
                    fileService.deleteFile(pfileMap);
                }
                
                if(!ObjectUtils.isEmpty(pfileMap.get("fileName")) && !ObjectUtils.isEmpty(pfileMap.get("fileContent")))
                    insertFileList.add(fileService.fileUploadByte(pfileMap.get("fileName").toString(), pfileMap.get("fileContent").toString(), fileGroupMap));
            }
        }

        paramMap.put("attachObid", Util.getAttachObid(attachFile, insertFileList));
        manageDao.updateJoinCompany(paramMap);
        fileDao.insertFile(insertFileList);
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
    public PageInfo<Map<String, Object>> selectJoinUserList(Map<String,Object> paramMap) throws Exception {
        PageHelper.startPage((int)paramMap.get("pageNum"), (int)paramMap.get("pageSize"), (String) paramMap.get("orderBy"));
        return manageDao.selectJoinUserList(paramMap);
    }

    public Map<String, Object> selectJoinUser(Map<String,Object> paramMap) throws Exception {
        Map<String,Object> returnMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = null;
        Map<String,Object> joinUserMap = manageDao.selectJoinUser(paramMap);

        if(ObjectUtils.isEmpty(joinUserMap)) throw new RuntimeException("일치하는 정보가 없습니다.");
        if(!ObjectUtils.isEmpty(joinUserMap.get("test3"))) joinUserMap.put("test3", Util.jsonToArray(Util.objToStr(joinUserMap.get("test3"))));
        if(!ObjectUtils.isEmpty(joinUserMap.get("attachObid"))){
            fileList = fileService.selectFileList(joinUserMap);
            joinUserMap.put("attachObid", Util.jsonToArray(Util.objToStr(joinUserMap.get("attachObid"))));
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
        List<Map<String,Object>> paramFileList = Util.objToList(paramMap.get("file"));

        fileGroupMap.put("fileGroup", "manage");
        fileGroupMap.put("fileSubGroup", "joinUser");

        if(!ObjectUtils.isEmpty(paramMap.get("test3"))) paramMap.put("test3",Util.objToJson(paramMap.get("test3")));
        if(!ObjectUtils.isEmpty(paramFileList)){
            for(Map<String,Object> paramFileMap:paramFileList){
                fileMap = fileService.fileUploadByte(Util.objToStr(paramFileMap.get("fileName")), Util.objToStr(paramFileMap.get("fileContent")), fileGroupMap);
                if(!ObjectUtils.isEmpty(fileMap)){
                    fileList.add(fileMap);
                    paramMap.put("attachObid", Util.getAttachObid(fileList));
                }
            }
        }

        manageDao.insertJoinUser(paramMap);
        fileDao.insertFile(fileList);
    } 

    @Transactional
    public void updateJoinUser(Map<String, Object> paramMap) throws Exception{
        Map<String,Object> joinUserMap = manageDao.selectJoinUser(paramMap);
        if(ObjectUtils.isEmpty(joinUserMap)) throw new RuntimeException("일치하는 정보가 없습니다."); 

        Map<String,Object> fileGroupMap = new HashMap<String,Object>();
        List<Map<String,Object>> insertFileList = new ArrayList<Map<String,Object>>();
        ArrayList<Object> attachFile = new ArrayList<Object>();
        List<Map<String,Object>> pFileList = Util.objToList(paramMap.get("file"));

        fileGroupMap.put("fileGroup", "manage");
        fileGroupMap.put("fileSubGroup", "joinUser");

        if(!ObjectUtils.isEmpty(paramMap.get("test3"))) paramMap.put("test3",Util.objToJson(paramMap.get("test3")));
        if(!ObjectUtils.isEmpty(joinUserMap.get("attachObid")))  attachFile = Util.jsonToArray(joinUserMap.get("attachObid").toString());
        if(!ObjectUtils.isEmpty(pFileList)){
            for(Map<String,Object> pfileMap : pFileList){
                if(!ObjectUtils.isEmpty(attachFile) && 
                    !ObjectUtils.isEmpty(pfileMap) && 
                    !ObjectUtils.isEmpty(pfileMap.get("fileName")) && 
                    !ObjectUtils.isEmpty(pfileMap.get("filePath")) && 
                    !ObjectUtils.isEmpty(pfileMap.get("obid")))
                {
                    attachFile.remove(pfileMap.get("obid"));
                    fileService.deleteFile(pfileMap);
                }
                
                if(!ObjectUtils.isEmpty(pfileMap.get("fileName")) && !ObjectUtils.isEmpty(pfileMap.get("fileContent")))
                    insertFileList.add(fileService.fileUploadByte(pfileMap.get("fileName").toString(), pfileMap.get("fileContent").toString(), fileGroupMap));
            }
        }

        paramMap.put("attachObid", Util.getAttachObid(attachFile, insertFileList));
        manageDao.updateJoinUser(paramMap);
        fileDao.insertFile(insertFileList);
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
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

    /*참가신청관리*/
    public PageInfo<Map<String, Object>> selectParticipateManageList(Map<String,Object> paramMap) throws Exception {
        List<Map<String,Object>> fileList = null;
        PageHelper.startPage((int)paramMap.get("pageNum"), (int)paramMap.get("pageSize"), (String) paramMap.get("orderBy"));
        PageInfo<Map<String, Object>> participateManageList = manageDao.selectParticipateManageList(paramMap);

        if(!ObjectUtils.isEmpty(participateManageList)){
            for(Map<String, Object> participateManageMap:participateManageList.getList()){
                if(!ObjectUtils.isEmpty(participateManageMap) && !ObjectUtils.isEmpty(participateManageMap.get("attatchFileObid"))){
                    Map<String, Object> fileParamMap = new HashMap<String, Object>();
                    fileParamMap.put("refObid", participateManageMap.get("attatchFileObid"));
                    fileList = fileService.selectFileList(fileParamMap);
                    if(!ObjectUtils.isEmpty(fileList)) participateManageMap.put("file", fileList);
                }
            }
        }

        return participateManageList;
    }

    public Map<String, Object> selectParticipateManage(Map<String,Object> paramMap) throws Exception {
        validateParticipate(paramMap, "S");

        Map<String,Object> fileParamMap = new HashMap<String,Object>();
        Map<String,Object> participateManageMap = manageDao.selectParticipateManage(paramMap);

        if(ObjectUtils.isEmpty(participateManageMap)) throw new RuntimeException("일치하는 정보가 없습니다.");
        //if(!ObjectUtils.isEmpty(participateManageMap.get("companyMainObjects"))) participateManageMap.put("companyMainObjects", Util.jsonToArray(Util.objToStr(participateManageMap.get("companyMainObjects"))));
        
        fileParamMap.put("refObid", participateManageMap.get("attatchFileObid"));
        List<Map<String,Object>> fileList = fileService.selectFileList(fileParamMap);
        participateManageMap.put("file", fileList);

        return participateManageMap;
    }

    @Transactional
    public void insertParticipateManage(Map<String, Object> paramMap) throws Exception{
        validateParticipate(paramMap, "I");

        Map<String,Object> fileMap = new HashMap<String,Object>();
        Map<String,Object> fileGroupMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> paramFileList = Util.objToList(paramMap.get("file"));

        fileGroupMap.put("fileGroup", "manage");
        fileGroupMap.put("fileSubGroup", "participateManage");

        //if(!ObjectUtils.isEmpty(paramMap.get("companyMainObjects"))) paramMap.put("companyMainObjects",Util.objToJson(paramMap.get("companyMainObjects")));
        if(!ObjectUtils.isEmpty(paramFileList)){
            paramMap.put("attatchFileObid", UUID.randomUUID().toString());

            for(Map<String,Object> paramFileMap:paramFileList){
                fileMap = fileService.fileUploadByte(paramFileMap.get("fileName").toString(), paramFileMap.get("fileContent").toString(), paramMap.get("attatchFileObid").toString(), fileGroupMap);
                if(!ObjectUtils.isEmpty(fileMap)){
                    fileList.add(fileMap);
                }
            }

            if(!ObjectUtils.isEmpty(fileList)) fileDao.insertFileList(fileList);
        }
        
        manageDao.insertParticipateManage(paramMap);
    } 

    @Transactional
    public void updateParticipateManage(Map<String, Object> paramMap) throws Exception{
        validateParticipate(paramMap, "U");

        Map<String,Object> participateManageMap = manageDao.selectParticipateManage(paramMap);
        if(ObjectUtils.isEmpty(participateManageMap)) throw new RuntimeException("일치하는 정보가 없습니다."); 

        Map<String,Object> fileGroupMap = new HashMap<String,Object>();
        List<Map<String,Object>> insertFileList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> pFileList = Util.objToList(paramMap.get("file"));

        fileGroupMap.put("fileGroup", "manage");
        fileGroupMap.put("fileSubGroup", "participate");

        //if(!ObjectUtils.isEmpty(paramMap.get("companyMainObjects"))) paramMap.put("companyMainObjects",Util.objToJson(paramMap.get("companyMainObjects")));
        if(!ObjectUtils.isEmpty(pFileList)){
            if(ObjectUtils.isEmpty(participateManageMap.get("attatchFileObid"))) 
                participateManageMap.put("attatchFileObid", UUID.randomUUID().toString());

            for(Map<String,Object> pfileMap : pFileList){
                if(!ObjectUtils.isEmpty(pfileMap) && !ObjectUtils.isEmpty(pfileMap.get("fileName")) && !ObjectUtils.isEmpty(pfileMap.get("filePath")) && !ObjectUtils.isEmpty(pfileMap.get("obid"))) 
                    fileService.deleteFile(pfileMap);

                if(!ObjectUtils.isEmpty(pfileMap.get("fileName")) && !ObjectUtils.isEmpty(pfileMap.get("fileContent")))
                    insertFileList.add(fileService.fileUploadByte(pfileMap.get("fileName").toString(), pfileMap.get("fileContent").toString(), participateManageMap.get("attatchFileObid").toString(), fileGroupMap));
            }
        }

        manageDao.updateParticipateManage(paramMap);
        fileDao.insertFileList(insertFileList);
    }

    @Transactional
    public void updateParticipateManageApproval(Map<String, Object> paramMap) throws Exception{
        validateParticipate(paramMap, "UA");

        Map<String,Object> participateManageMap = manageDao.selectParticipateManage(paramMap);
        if(ObjectUtils.isEmpty(participateManageMap)) throw new RuntimeException("일치하는 정보가 없습니다."); 
        
        manageDao.updateParticipateManageApproval(paramMap);
    }

    @Transactional
    public void updateParticipateManageApprovalList(Map<String, Object> paramMap) throws Exception{
        validateParticipate(paramMap, "UL");
            
        List<Map<String,Object>> approvalList = Util.objToList(paramMap.get("list"));
        
        for(Map<String,Object> approvalMap : approvalList){
            if(!ObjectUtils.isEmpty(approvalMap) && !ObjectUtils.isEmpty(approvalMap.get("masterLicenseListObid")) && !ObjectUtils.isEmpty(approvalMap.get("email"))){
                approvalMap.put("approvalCode", paramMap.get("approvalCode"));
                manageDao.updateParticipateManageApproval(approvalMap);
            }
        }
    }

    @Transactional
    public void deleteParticipateManage(Map<String, Object> paramMap) throws Exception{
        validateParticipate(paramMap, "D");

        Map<String,Object> participateManageMap = manageDao.selectParticipateManage(paramMap);

        if(ObjectUtils.isEmpty(participateManageMap)) 
            throw new RuntimeException("일치하는 정보가 없습니다.");        

        if(!ObjectUtils.isEmpty(participateManageMap.get("attatchFileObid"))){
            Map<String,Object> fileParamMap = new HashMap<String, Object>();
            fileParamMap.put("refObid", participateManageMap.get("attatchFileObid"));
            fileService.deleteFileList(fileParamMap);
        }

        manageDao.deleteParticipateManage(participateManageMap);
    }

    public void validateParticipate(Map<String, Object> paramMap, String type){
        if(ObjectUtils.isEmpty(paramMap)) throw new RuntimeException("입력 데이터를 확인하시기 바랍니다.");
        if(type.equals("S")){
            if(ObjectUtils.isEmpty(paramMap.get("masterLicenseListObid"))) throw new RuntimeException("마스터 라이센스 정보를 확인하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("email"))) throw new RuntimeException("이메일을 확인하시기 바랍니다.");
        }
        if(type.equals("I") || type.equals("U")){
            if(ObjectUtils.isEmpty(paramMap.get("masterLicenseListObid"))) throw new RuntimeException("마스터 라이센스 정보를 확인하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("email"))) throw new RuntimeException("이메일을 확인하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("name"))) throw new RuntimeException("닉네임을 입력하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("userType"))) throw new RuntimeException("회원요형을 입력하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("approvalCode"))) throw new RuntimeException("참가여부를 선택하세요.");
        }
        if(type.equals("UA")){
            if(ObjectUtils.isEmpty(paramMap.get("masterLicenseListObid"))) throw new RuntimeException("마스터 라이센스 정보를 확인하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("email"))) throw new RuntimeException("이메일을 확인하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("approvalCode"))) throw new RuntimeException("참가여부를 선택하세요.");
        }
        if(type.equals("UL")){
            if(ObjectUtils.isEmpty(paramMap.get("list"))) throw new RuntimeException("마스터 라이센스 정보를 확인하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("approvalCode"))) throw new RuntimeException("참가여부를 선택하세요.");
        }
        if(type.equals("D")){
            if(ObjectUtils.isEmpty(paramMap.get("masterLicenseListObid"))) throw new RuntimeException("마스터 라이센스 정보를 확인하시기 바랍니다.");
        }
    }

    /*권한관리*/
    public PageInfo<Map<String, Object>> selectAuthManageList(Map<String,Object> paramMap) throws Exception {
        PageHelper.startPage((int)paramMap.get("pageNum"), (int)paramMap.get("pageSize"), (String) paramMap.get("orderBy"));
        return manageDao.selectAuthManageList(paramMap);
    }

    public Map<String, Object> selectAuthManage(Map<String,Object> paramMap) throws Exception {
        validateAuth(paramMap, "S");
        return manageDao.selectAuthManage(paramMap);
    }

    @Transactional
    public void insertAuthManage(Map<String, Object> paramMap) throws Exception{
        validateAuth(paramMap, "I");
        manageDao.insertAuthManage(paramMap);
    }

    @Transactional
    public void updateAuthManage(Map<String, Object> paramMap) throws Exception{
        validateAuth(paramMap, "U");

        Map<String,Object> authManageMap = manageDao.selectAuthManage(paramMap);

        if(ObjectUtils.isEmpty(authManageMap))
            throw new RuntimeException("일치하는 정보가 없습니다."); 

        manageDao.updateAuthManage(paramMap);
    }

    @Transactional
    public void updateAuthManageAuthList(Map<String, Object> paramMap) throws Exception{
        validateAuth(paramMap, "UL");

        List<Map<String,Object>> authManageList = Util.objToList(paramMap.get("list"));
    
        for(Map<String,Object> authManageMap : authManageList){
            if(!ObjectUtils.isEmpty(authManageMap) && !ObjectUtils.isEmpty(authManageMap.get("masterLicenseListObid")) && 
               !ObjectUtils.isEmpty(authManageMap.get("email")) && !ObjectUtils.isEmpty(authManageMap.get("grade")))
                manageDao.updateAuthManageAuth(authManageMap);
        }
    }

    @Transactional
    public void deleteAuthManage(Map<String, Object> paramMap) throws Exception{
        validateAuth(paramMap, "D");

        Map<String,Object> authManageMap = manageDao.selectAuthManage(paramMap);
        if(ObjectUtils.isEmpty(authManageMap)) throw new RuntimeException("일치하는 정보가 없습니다.");

        manageDao.deleteAuthManage(authManageMap);
    }

    @Transactional
    public void deleteAuthManageList(Map<String, Object> paramMap) throws Exception{
        validateAuth(paramMap, "DL");

        List<Map<String,Object>> authManageList = Util.objToList(paramMap.get("list"));
        
        for(Map<String,Object> authManageMap : authManageList){
            if(!ObjectUtils.isEmpty(authManageMap) && 
               !ObjectUtils.isEmpty(authManageMap.get("masterLicenseListObid")) && 
               !ObjectUtils.isEmpty(authManageMap.get("email")))
                manageDao.deleteAuthManage(authManageMap);
        }
    }

    public void validateAuth(Map<String, Object> paramMap, String type){
        if(ObjectUtils.isEmpty(paramMap)) throw new RuntimeException("데이터를 입력하세요.");
        if(type.equals("I") || type.equals("U")){
            if(ObjectUtils.isEmpty(paramMap.get("masterLicenseListObid")))  throw new RuntimeException("마스터 라이센트 리스트를 선택하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("email")))  throw new RuntimeException("이메일을 선택하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("name"))) throw new RuntimeException("이름을 입력하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("reqGrade"))) throw new RuntimeException("요청권한을 선택하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("grade"))) throw new RuntimeException("권한등급을 선택하세요.");
        }
        if(type.equals("S") || type.equals("D")){
            if(ObjectUtils.isEmpty(paramMap.get("masterLicenseListObid")))  throw new RuntimeException("마스터 라이센트 리스트를 선택하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("email")))  throw new RuntimeException("이메일을 선택하시기 바랍니다.");
        }
        if(type.equals("DL") || type.equals("UL")){
            if(ObjectUtils.isEmpty(paramMap.get("list"))) throw new RuntimeException("대상을 선택하시기 바랍니다.");
        }
    }

    /*3D제작요청관리*/
    public PageInfo<Map<String, Object>> select3DManageList(Map<String,Object> paramMap) throws Exception {
        PageHelper.startPage((int)paramMap.get("pageNum"), (int)paramMap.get("pageSize"), (String) paramMap.get("orderBy"));
        return manageDao.select3DManageList(paramMap);
    }

    public Map<String, Object> select3DManage(Map<String,Object> paramMap) throws Exception {
        validate3D(paramMap, "S");
        
        Map<String, Object> fileParamMap = new HashMap<String, Object>();
        Map<String, Object> resultMap = manageDao.select3DManage(paramMap);
        if(ObjectUtils.isEmpty(resultMap)) throw new RuntimeException("일치하는 정보가 없습니다.");
        
        fileParamMap.put("refObid", resultMap.get("attatchFileObid"));
        List<Map<String,Object>> fileList = fileService.selectFileList(fileParamMap);
        resultMap.put("file", fileList);

        fileParamMap.put("refObid", resultMap.get("imgAttatchFileObid"));
        List<Map<String,Object>> imgFileList = fileService.selectFileList(fileParamMap);
        resultMap.put("imgFile", imgFileList);

        return resultMap;
    }

    @Transactional
    public void insert3DManage(Map<String, Object> paramMap) throws Exception{
        validate3D(paramMap, "I");

        Map<String,Object> fileMap = new HashMap<String,Object>();
        Map<String,Object> fileGroupMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> imgFileList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> fileParamList = Util.objToList(paramMap.get("file"));
        List<Map<String,Object>> imgFileParamList = Util.objToList(paramMap.get("imgFile"));

        fileGroupMap.put("fileGroup", "manage");
        fileGroupMap.put("fileSubGroup", "3D");
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
            paramMap.put("imgAttatchFileObid", UUID.randomUUID().toString());

            for(Map<String,Object> imgFileParamMap:imgFileParamList){
                fileMap = fileService.fileUploadByte(imgFileParamMap.get("fileName").toString(), imgFileParamMap.get("fileContent").toString(), paramMap.get("imgAttatchFileObid").toString(), fileGroupMap);
                if(!ObjectUtils.isEmpty(fileMap)){
                    imgFileList.add(fileMap);
                }
            }

            if(!ObjectUtils.isEmpty(imgFileList)) fileDao.insertFileList(imgFileList);
        }
        
        manageDao.insert3DManage(paramMap);
    }

    @Transactional
    public void update3DManage(Map<String, Object> paramMap) throws Exception{
        validate3D(paramMap, "U");

        Map<String,Object> map = manageDao.select3DManage(paramMap);

        if(ObjectUtils.isEmpty(map)) 
            throw new RuntimeException("일치하는 정보가 없습니다."); 

        Map<String,Object> fileGroupMap = new HashMap<String,Object>();
        List<Map<String,Object>> insertFileList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> insertImgFileList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> pFileList = Util.objToList(paramMap.get("file"));
        List<Map<String,Object>> pImgFileList = Util.objToList(paramMap.get("imgFile"));

        fileGroupMap.put("fileGroup", "manage");
        fileGroupMap.put("fileSubGroup", "participate");

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
            if(ObjectUtils.isEmpty(map.get("imgAttatchFileObid"))) map.put("imgAttatchFileObid", UUID.randomUUID().toString());

            for(Map<String,Object> pImgfileMap : pImgFileList){
                if(!ObjectUtils.isEmpty(pImgfileMap) && !ObjectUtils.isEmpty(pImgfileMap.get("fileName")) && !ObjectUtils.isEmpty(pImgfileMap.get("filePath")) && !ObjectUtils.isEmpty(pImgfileMap.get("obid"))) 
                    fileService.deleteFile(pImgfileMap);

                if(!ObjectUtils.isEmpty(pImgfileMap.get("fileName")) && !ObjectUtils.isEmpty(pImgfileMap.get("fileContent")))
                    insertImgFileList.add(fileService.fileUploadByte(pImgfileMap.get("fileName").toString(), pImgfileMap.get("fileContent").toString(), map.get("imgAttatchFileObid").toString(), fileGroupMap));
            }

            if(!ObjectUtils.isEmpty(insertImgFileList)) fileDao.insertFileList(insertImgFileList);
        }

        paramMap.put("attatchFileObid", map.get("attatchFileObid"));
        paramMap.put("imgAttatchFileObid", map.get("imgAttatchFileObid"));
        manageDao.update3DManage(paramMap);
    }

    @Transactional
    public void delete3DManage(Map<String, Object> paramMap) throws Exception{
        validate3D(paramMap, "D");
        Map<String, Object> map = null;
        Map<String,Object> fileParamMap = null;
        
        if(!ObjectUtils.isEmpty(paramMap) && !ObjectUtils.isEmpty(paramMap.get("obid")))
        {
            map = manageDao.select3DManage(paramMap);
            fileParamMap = new HashMap<String, Object>();
            fileParamMap.put("refObid", map.get("attatchFileObid"));
            fileService.deleteFileList(fileParamMap);
            fileParamMap.put("refObid", map.get("imgAttatchFileObid"));
            fileService.deleteFileList(fileParamMap);
        }

        manageDao.delete3DManage(paramMap);
    }

    @Transactional
    public void delete3DManageList(Map<String, Object> paramMap) throws Exception{
        validate3D(paramMap, "DL");
        Map<String, Object> map = null;
        Map<String,Object> fileParamMap = null;

        List<Map<String,Object>> authManageList = Util.objToList(paramMap.get("list"));
        
        for(Map<String,Object> authManageMap : authManageList){
            if(!ObjectUtils.isEmpty(authManageMap) && !ObjectUtils.isEmpty(authManageMap.get("obid")))
            {
                map = manageDao.select3DManage(authManageMap);
                fileParamMap = new HashMap<String, Object>();
                fileParamMap.put("refObid", map.get("attatchFileObid"));
                fileService.deleteFileList(fileParamMap);
                fileParamMap.put("refObid", map.get("imgAttatchFileObid"));
                fileService.deleteFileList(fileParamMap);
            }
        }

        manageDao.delete3DManageList(authManageList);
    }

    public void validate3D(Map<String, Object> paramMap, String type){
        if(ObjectUtils.isEmpty(paramMap)) throw new RuntimeException("데이터를 입력하세요.");
        if(type.equals("I")){
            if(ObjectUtils.isEmpty(paramMap.get("masterLicenseListObid")))  throw new RuntimeException("행사를 선택하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("email")))  throw new RuntimeException("이메일을 선택하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("fileName"))) throw new RuntimeException("파일명을 입력하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("fileType"))) throw new RuntimeException("파일분류를 선택하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("fileDescript"))) throw new RuntimeException("파일설명을 입력하세요.");
        }
        if(type.equals("U")){
            if(ObjectUtils.isEmpty(paramMap.get("obid")))  throw new RuntimeException("선택된 3D제약요청이 없습니다.");
            if(ObjectUtils.isEmpty(paramMap.get("masterLicenseListObid")))  throw new RuntimeException("행사를 선택하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("email")))  throw new RuntimeException("이메일을 선택하시기 바랍니다.");
            if(ObjectUtils.isEmpty(paramMap.get("fileName"))) throw new RuntimeException("파일명을 입력하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("fileType"))) throw new RuntimeException("파일분류를 선택하세요.");
            if(ObjectUtils.isEmpty(paramMap.get("fileDescript"))) throw new RuntimeException("파일설명을 입력하세요.");
        }
        if(type.equals("S") || type.equals("D")){
            if(ObjectUtils.isEmpty(paramMap.get("obid")))  throw new RuntimeException("선택된 3D제약요청이 없습니다.");
        }
        if(type.equals("DL") || type.equals("UL")){
            if(ObjectUtils.isEmpty(paramMap.get("list"))) throw new RuntimeException("대상을 선택하시기 바랍니다.");
        }
    }
}
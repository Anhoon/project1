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
import kr.co.smartcube.xcube.mybatis.dao.ParticipateDao;
import kr.co.smartcube.xcube.util.Util;

@Service
public class ParticipateService {

    @Autowired
    private ParticipateDao participateDao;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileDao fileDao;

    /*참가신청관리*/
    public PageInfo<Map<String, Object>> selectParticipateManageList(Map<String,Object> paramMap) throws Exception {
        PageHelper.startPage((int)paramMap.get("pageNum"), (int)paramMap.get("pageSize"), (String) paramMap.get("orderBy"));
        PageInfo<Map<String, Object>> participateManageList = participateDao.selectParticipateManageList(paramMap);
        return participateManageList;
    }

    public Map<String, Object> selectParticipateManage(Map<String,Object> paramMap) throws Exception {
        validateParticipate(paramMap, "S");

        Map<String,Object> fileParamMap = new HashMap<String,Object>();
        Map<String,Object> participateManageMap = participateDao.selectParticipateManage(paramMap);

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

        fileGroupMap.put("fileGroup", "participate");
        fileGroupMap.put("fileSubGroup", "apply");

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
        
        participateDao.insertParticipateManage(paramMap);
    } 

    @Transactional
    public void updateParticipateManage(Map<String, Object> paramMap) throws Exception{
        validateParticipate(paramMap, "U");

        Map<String,Object> participateManageMap = participateDao.selectParticipateManage(paramMap);
        if(ObjectUtils.isEmpty(participateManageMap)) throw new RuntimeException("일치하는 정보가 없습니다."); 

        Map<String,Object> fileGroupMap = new HashMap<String,Object>();
        List<Map<String,Object>> insertFileList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> pFileList = Util.objToList(paramMap.get("file"));

        fileGroupMap.put("fileGroup", "participate");
        fileGroupMap.put("fileSubGroup", "apply");

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

        participateDao.updateParticipateManage(paramMap);
        fileDao.insertFileList(insertFileList);
    }

    @Transactional
    public void updateParticipateManageApproval(Map<String, Object> paramMap) throws Exception{
        validateParticipate(paramMap, "UA");

        Map<String,Object> participateManageMap = participateDao.selectParticipateManage(paramMap);
        if(ObjectUtils.isEmpty(participateManageMap)) throw new RuntimeException("일치하는 정보가 없습니다."); 
        
        participateDao.updateParticipateManageApproval(paramMap);
    }

    @Transactional
    public void updateParticipateManageApprovalList(Map<String, Object> paramMap) throws Exception{
        validateParticipate(paramMap, "UL");
            
        List<Map<String,Object>> approvalList = Util.objToList(paramMap.get("list"));
        
        for(Map<String,Object> approvalMap : approvalList){
            if(!ObjectUtils.isEmpty(approvalMap) && !ObjectUtils.isEmpty(approvalMap.get("masterLicenseListObid")) && !ObjectUtils.isEmpty(approvalMap.get("email"))){
                approvalMap.put("approvalCode", paramMap.get("approvalCode"));
                participateDao.updateParticipateManageApproval(approvalMap);
            }
        }
    }

    @Transactional
    public void deleteParticipateManage(Map<String, Object> paramMap) throws Exception{
        validateParticipate(paramMap, "D");

        Map<String,Object> participateManageMap = participateDao.selectParticipateManage(paramMap);

        if(ObjectUtils.isEmpty(participateManageMap)) 
            throw new RuntimeException("일치하는 정보가 없습니다.");        

        if(!ObjectUtils.isEmpty(participateManageMap.get("attatchFileObid"))){
            Map<String,Object> fileParamMap = new HashMap<String, Object>();
            fileParamMap.put("refObid", participateManageMap.get("attatchFileObid"));
            fileService.deleteFileList(fileParamMap);
        }

        participateDao.deleteParticipateManage(participateManageMap);
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
}
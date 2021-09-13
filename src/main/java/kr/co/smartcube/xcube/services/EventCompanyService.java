package kr.co.smartcube.xcube.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.github.pagehelper.PageHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.smartcube.xcube.mybatis.dao.EventCompanyDao;
import kr.co.smartcube.xcube.mybatis.dao.FileDao;
import kr.co.smartcube.xcube.util.Util;

@Service
public class EventCompanyService {

    @Autowired
    private EventCompanyDao eventCompanyDao;

    @Autowired
    private FileUploadDownloadService fileService;

    @Autowired
    private FileDao fileDao;

    public List<Map<String, Object>> selectMasterLicenseList(Map<String,Object> map) {
        PageHelper.startPage((int)map.get("pageNum"), (int)map.get("pageSize"), (String) map.get("orderBy"));
        return eventCompanyDao.selectMasterLicenseList(map);
    }

    public Map<String, Object> selectMasterLicense(Map<String, Object> map) {
        Map<String, Object> resultMap = eventCompanyDao.selectMasterLicense(map);
        if(Util.isEmpty(resultMap)) throw new RuntimeException("일치하는 정보가 없습니다."); 
        return resultMap;
    }
    
    @Transactional
    public void insertMasterLicense(Map<String, Object> map) throws Exception{
        Map<String,Object> fileMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        Map<String,Object> masterLicense = new HashMap<String,Object>();
        Map<String,Object> masterLicenseFileMap = Util.objToMap(map.get("files"));
        if(Util.isEmpty(map.get("email"))){
            throw new RuntimeException("이메일 정보를 입력해 주세요.");
        }
        masterLicense.putAll(map);
        masterLicense.remove("masterList");
        String obid = UUID.randomUUID().toString();
        masterLicense.put("obid", obid);
        if(!Util.isEmpty(masterLicenseFileMap)){
            fileMap = fileService.fileUploadByte(Util.objToStr(masterLicenseFileMap.get("fileName")), Util.objToStr(masterLicenseFileMap.get("fileContent")));
            masterLicense.put("attatchObid", fileMap.get("obid"));
            fileList.add(fileMap);
        }
        List<Map<String,Object>> masterLicenseList = Util.objToList(map.get("masterLicenseLists"));
        Map<String,Object> masterLicenseListFileMap = new HashMap<String,Object>();
        for(int i=0; i<masterLicenseList.size(); i++){
            Map<String,Object> listMap = masterLicenseList.get(i);
            if(Util.isEmpty(listMap.get("status"))){
                throw new RuntimeException("라이센스리스트 상태 정보를 입력해 주세요.");
            }
            if(Util.isEmpty(listMap.get("startDate"))){
                throw new RuntimeException("라이센스 시작개최기간 정보를 입력해 주세요.");
            }
            if(Util.isEmpty(listMap.get("endDate"))){
                throw new RuntimeException("라이센스 종료개최기간 정보를 입력해 주세요.");
            }
            if(!Util.dateCheck(listMap.get("startDate"))){
                throw new RuntimeException("라이센스 시작개최기간 날짜 형식이 맞지 않습니다.");
            }
            if(!Util.dateCheck(listMap.get("endDate"))){
                throw new RuntimeException("라이센스 종료개최기간 날짜 형식이 맞지 않습니다.");
            }
            masterLicenseListFileMap = Util.objToMap(listMap.get("files"));
            if(!Util.isEmpty(masterLicenseListFileMap)){
                fileMap = fileService.fileUploadByte(Util.objToStr(masterLicenseListFileMap.get("fileName")), Util.objToStr(masterLicenseListFileMap.get("fileContent")));
                listMap.put("attatchObid", fileMap.get("obid"));
                fileList.add(fileMap);
            }            
            listMap.put("obid", UUID.randomUUID().toString());
            listMap.put("refObid", obid);
        }
        eventCompanyDao.insertMasterLicense(masterLicense);
        eventCompanyDao.insertMasterLicenseList(masterLicenseList);
        fileDao.insertFile(fileList);
    }

    @Transactional
    public void updateMasterLicense(Map<String, Object> map){
        int i = eventCompanyDao.updateMasterLicense(map);
        if(i == 0) throw new RuntimeException("일치하는 정보가 없습니다."); 
    }

    @Transactional
    public void updateMasterLicenseList(Map<String, Object> map){
        int i = eventCompanyDao.updateMasterLicenseList(map);
        if(i == 0) throw new RuntimeException("일치하는 정보가 없습니다.");
	}
}
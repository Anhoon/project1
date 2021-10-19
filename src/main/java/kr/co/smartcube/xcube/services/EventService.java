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

import kr.co.smartcube.xcube.mybatis.dao.EventDao;
import kr.co.smartcube.xcube.mybatis.dao.FileDao;
import kr.co.smartcube.xcube.util.Util;

@Service
public class EventService {

    @Autowired 
    private EventDao eventDao;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileDao fileDao;

    public PageInfo<Map<String, Object>> selectEventList(Map<String,Object> map) {
        PageHelper.startPage((int)map.get("pageNum"), (int)map.get("pageSize"), (String) map.get("orderBy"));
        return eventDao.selectEventList(map);
    }

    public Map<String, Object> selectEvent(Map<String,Object> map) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        Map<String, Object> eventMap = eventDao.selectEvent(map);
        if(ObjectUtils.isEmpty(eventMap)) throw new RuntimeException("행사등록 정보가 없습니다.");
        //파일이 있을경우 추가
        Map<String,Object> fileParam = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        fileParam.put("refObid", eventMap.get("attatchFileObid"));
        fileList = fileDao.selectFileList(fileParam);
        if(!ObjectUtils.isEmpty(fileList)) eventMap.put("files", fileList);
        //파일이 있을경우 추가
        
        Map<String, Object> participateListMap = eventDao.selectParticipate(map);
        if(ObjectUtils.isEmpty(participateListMap)) throw new RuntimeException("참가모집 정보가 없습니다.");
        fileParam.put("refObid", participateListMap.get("attatchFileObid"));
        fileList = fileDao.selectFileList(fileParam);
        if(!ObjectUtils.isEmpty(fileList)) participateListMap.put("files", fileList);
        resultMap.put("eventLists", eventMap);
        resultMap.put("participateLists", participateListMap);
        return resultMap;
    }
    
    @Transactional
    public void insertEventList(Map<String, Object> map) throws Exception{
        Map<String,Object> eventMap = Util.objToMap(map.get("eventLists"));
        Map<String,Object> participateMap = Util.objToMap(map.get("participateLists"));
        if(ObjectUtils.isEmpty(eventMap)) throw new RuntimeException("행사등록 정보가 없습니다."); 
        if(ObjectUtils.isEmpty(participateMap)) throw new RuntimeException("참가모집 정보가 없습니다."); 
        insertEvent(eventMap); //행사등록 정보
        insertParticipate(participateMap); //참가모집 정보
    }

    @Transactional
    public void updateEventList(Map<String, Object> map) throws Exception{
        Map<String,Object> eventMap = Util.objToMap(map.get("eventLists"));
        Map<String,Object> participateMap = Util.objToMap(map.get("participateLists"));
        if(ObjectUtils.isEmpty(eventMap)) throw new RuntimeException("행사등록 정보가 없습니다."); 
        if(ObjectUtils.isEmpty(participateMap)) throw new RuntimeException("참가모집 정보가 없습니다."); 
        int i = updateEvent(eventMap); //행사등록 정보
        i = i + updateParticipate(participateMap); //참가모집 정보
        if(i == 0) throw new RuntimeException("일치하는 정보가 없습니다."); 
    }
    @Transactional
    public void deleteEventList(Map<String, Object> map) throws Exception{
        if(ObjectUtils.isEmpty(map.get("masterLicenseListObid"))) throw new RuntimeException("MASTER_LICENSE_LIST_OBID 정보가 없습니다."); 
        int i = eventDao.deleteEvent(map); //행사등록 정보
        i = i + eventDao.deleteParticipate(map); //참가모집 정보
        if(i == 0) throw new RuntimeException("일치하는 정보가 없습니다."); 
    }
    

    public void insertEvent(Map<String, Object> map) throws Exception{
        Map<String,Object> fileMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> paramFileList = Util.objToList(map.get("files"));
        if(!ObjectUtils.isEmpty(paramFileList)){
            String attatchObid = UUID.randomUUID().toString();
            map.put("attatchFileObid", attatchObid);
            for(Map<String,Object> paramFileMap:paramFileList){
                fileMap = fileService.fileUploadByte(Util.objToStr(paramFileMap.get("fileName")), Util.objToStr(paramFileMap.get("fileContent")));
                if(!ObjectUtils.isEmpty(fileMap)){
                    fileMap.put("refObid", attatchObid);
                    fileList.add(fileMap);
                }
            }
        }
        validationEventCheck(map);
        eventDao.insertEvent(map);
        if(!ObjectUtils.isEmpty(fileList)) fileDao.insertFileList(fileList);
    }    

    public void insertParticipate(Map<String, Object> map) throws Exception{
        Map<String,Object> fileMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> paramFileList = Util.objToList(map.get("files"));
        if(!ObjectUtils.isEmpty(paramFileList)){
            String attatchObid = UUID.randomUUID().toString();
            map.put("attatchFileObid", attatchObid);
            for(Map<String,Object> paramFileMap:paramFileList){
                fileMap = fileService.fileUploadByte(Util.objToStr(paramFileMap.get("fileName")), Util.objToStr(paramFileMap.get("fileContent")));
                if(!ObjectUtils.isEmpty(fileMap)){
                    fileMap.put("refObid", attatchObid);
                    fileList.add(fileMap);
                }
            }
        }
        validationParticipateCheck(map);
        eventDao.insertParticipate(map);
        if(!ObjectUtils.isEmpty(fileList)) fileDao.insertFileList(fileList);
    }

    public int updateEvent(Map<String, Object> map) throws Exception{
        Map<String,Object> fileMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        Map<String,Object> orgEvent = eventDao.selectEvent(map);
        List<Map<String,Object>> paramFileList = Util.objToList(map.get("files"));
        if(ObjectUtils.isEmpty(orgEvent)) throw new RuntimeException("일치하는 정보가 없습니다."); 
        if(!ObjectUtils.isEmpty(paramFileList)){
            for(Map<String,Object> paramFileMap:paramFileList){
                if(!Util.objToStr(orgEvent.get("fileName")).equals(Util.objToStr(paramFileMap.get("fileName")))){
                    String attatchObid = UUID.randomUUID().toString();
                    map.put("attatchFileObid", attatchObid);
                    fileMap = fileService.fileUploadByte(Util.objToStr(paramFileMap.get("fileName")), Util.objToStr(paramFileMap.get("fileContent")));
                    fileMap.put("refObid", attatchObid);
                    fileList.add(fileMap);
                }else{
                    map.put("attatchFileObid", orgEvent.get("attatchFileObid"));
                }
            }
        }
        validationEventCheck(map);
        int i = eventDao.updateEvent(map);
        if(!ObjectUtils.isEmpty(fileList)) fileDao.insertFileList(fileList);
        return i;
    }    

    public int updateParticipate(Map<String, Object> map) throws Exception{
        Map<String,Object> fileMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        Map<String,Object> orgEvent = eventDao.selectParticipate(map);
        List<Map<String,Object>> paramFileList = Util.objToList(map.get("files"));
        if(ObjectUtils.isEmpty(orgEvent)) throw new RuntimeException("일치하는 정보가 없습니다."); 
        if(!ObjectUtils.isEmpty(paramFileList)){
            for(Map<String,Object> paramFileMap:paramFileList){
                if(!Util.objToStr(orgEvent.get("fileName")).equals(Util.objToStr(paramFileMap.get("fileName")))){
                    String attatchObid = UUID.randomUUID().toString();
                    map.put("attatchFileObid", attatchObid);
                    fileMap = fileService.fileUploadByte(Util.objToStr(paramFileMap.get("fileName")), Util.objToStr(paramFileMap.get("fileContent")));
                    fileMap.put("refObid", attatchObid);
                    fileList.add(fileMap);
                }else{
                    map.put("attatchFileObid", orgEvent.get("attatchFileObid"));
                }
            }
        }
        validationParticipateCheck(map);
        int i = eventDao.updateParticipate(map);
        if(!ObjectUtils.isEmpty(fileList)) fileDao.insertFileList(fileList);
        return i;
    }
    public void validationEventCheck(Map<String,Object> map){
        String name = "행사등록";
        if(ObjectUtils.isEmpty(map.get("masterLicenseListObid"))){
            throw new RuntimeException(name +" masterLicenseListObid 정보가 없습니다.");
        }
        if(ObjectUtils.isEmpty(map.get("title"))){
            throw new RuntimeException(name +" 행사타이틀 정보를 입력해 주세요.");
        }
        /*
        if(ObjectUtils.isEmpty(map.get("enTitle"))){
            throw new RuntimeException(name +" 영문타이틀 정보를 입력해 주세요.");
        }
        if(ObjectUtils.isEmpty(map.get("alias"))){
            throw new RuntimeException(name +" 약칭 정보를 입력해 주세요.");
        }
        if(ObjectUtils.isEmpty(map.get("objects"))){
            throw new RuntimeException(name +" 행사대상 정보를 입력해 주세요.");
        }
        if(ObjectUtils.isEmpty(map.get("contents"))){
            throw new RuntimeException(name +" 행사내용 입력해 주세요.");
        }
        if(ObjectUtils.isEmpty(map.get("showProducts"))){
            throw new RuntimeException(name +" 전시품목 정보를 입력해 주세요.");
        }
        if(ObjectUtils.isEmpty(map.get("organizer"))){
            throw new RuntimeException(name +" 주최 정보를 입력해 주세요.");
        }
        if(ObjectUtils.isEmpty(map.get("subjectivity"))){
            throw new RuntimeException(name +" 주관 정보를 입력해 주세요.");
        }
        if(ObjectUtils.isEmpty(map.get("support"))){
            throw new RuntimeException(name +" 후원 정보를 입력해 주세요.");
        }
        if(ObjectUtils.isEmpty(map.get("hompage"))){
            throw new RuntimeException(name +" 홈페이지 정보를 입력해 주세요.");
        }
        */
    }   

    public void validationParticipateCheck(Map<String,Object> map){
        String name = "참가모집";
        if(ObjectUtils.isEmpty(map.get("masterLicenseListObid"))){
            throw new RuntimeException(name +" masterLicenseListObid 정보가 없습니다.");
        }
        if(ObjectUtils.isEmpty(map.get("scale"))){
            throw new RuntimeException(name +" 참가규모 정보를 입력해 주세요.");
        }
        /*
        if(ObjectUtils.isEmpty(map.get("applyType"))){
            throw new RuntimeException(name +" 모집인원타입 정보를 입력해 주세요.");
        }
        if(ObjectUtils.isEmpty(map.get("applyCount"))){
            throw new RuntimeException(name +" 모집인원 정보를 입력해 주세요.");
        }
        if(ObjectUtils.isEmpty(map.get("applyFee"))){
            throw new RuntimeException(name +" 참가비 종료일 정보를 입력해 주세요.");
        }
        */
        if(ObjectUtils.isEmpty(map.get("startApplyDate"))){
            throw new RuntimeException(name +" 신청기간 시작일 정보를 입력해 주세요.");
        }
        if(ObjectUtils.isEmpty(map.get("endApplyDate"))){
            throw new RuntimeException(name +" 신청기가 종료일 정보를 입력해 주세요.");
        }
        if(!Util.dateCheck(map.get("startApplyDate"))){
            throw new RuntimeException(name +" 신청기간 시작일 날짜 형식이 맞지 않습니다.");
        }
        if(!Util.dateCheck(map.get("endApplyDate"))){
            throw new RuntimeException(name +" 신청기간 종료일 날짜 형식이 맞지 않습니다.");
        }
    }
}
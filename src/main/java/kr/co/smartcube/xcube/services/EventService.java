package kr.co.smartcube.xcube.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Map<String, Object>> selectEventList(Map<String,Object> map) {
        PageHelper.startPage((int)map.get("pageNum"), (int)map.get("pageSize"), (String) map.get("orderBy"));
        return eventDao.selectEventList(map);
    }

    public Map<String, Object> selectEvent(Map<String,Object> map) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        Map<String, Object> eventMap = eventDao.selectEvent(map);
        if(Util.isEmpty(eventMap)) throw new RuntimeException("일치하는 정보가 없습니다.");
        //파일이 있을경우 추가
        Map<String,Object> fileParam = new HashMap<String,Object>();
        Map<String,Object> fileMap = new HashMap<String,Object>();
        fileParam.put("attachObid", eventMap.get("attatchObid"));
        fileMap = fileDao.selectFile(fileParam);
        if(!Util.isEmpty(fileMap)) eventMap.put("files", fileMap);
        //파일이 있을경우 추가
        Map<String, Object> participateListMap = eventDao.selectParticipate(map);
        fileParam.put("attachObid", participateListMap.get("attatchObid"));
        fileMap = fileDao.selectFile(fileParam);
        if(!Util.isEmpty(fileMap)) participateListMap.put("files", fileMap);

        resultMap.put("eventLists", eventMap);
        resultMap.put("participateLists", participateListMap);
        
        return resultMap;
    }
    
    @Transactional
    public void insertEventList(Map<String, Object> map) throws Exception{
        Map<String,Object> eventMap = Util.objToMap(map.get("eventLists"));
        Map<String,Object> participateMap = Util.objToMap(map.get("participateLists"));
        if(Util.isEmpty(eventMap)) throw new RuntimeException("행사등록 정보가 없습니다."); 
        if(Util.isEmpty(participateMap)) throw new RuntimeException("참가모집 정보가 없습니다."); 
        insertEvent(eventMap); //행사등록 정보
        insertParticipate(participateMap); //참가모집 정보
    }

    @Transactional
    public void updateEvent(Map<String, Object> map) throws Exception{
        Map<String,Object> eventMap = Util.objToMap(map.get("eventLists"));
        Map<String,Object> participateMap = Util.objToMap(map.get("participateLists"));
        if(Util.isEmpty(eventMap)) throw new RuntimeException("행사등록 정보가 없습니다."); 
        if(Util.isEmpty(participateMap)) throw new RuntimeException("참가모집 정보가 없습니다."); 
        int i = updateEventList(eventMap); //행사등록 정보
        i = i + updateParticipateList(participateMap); //참가모집 정보
        if(i == 0) throw new RuntimeException("일치하는 정보가 없습니다."); 
    }

    public void insertEvent(Map<String, Object> map) throws Exception{
        Map<String,Object> fileMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        Map<String,Object> eventFileMap = Util.objToMap(map.get("files"));
        if(!Util.isEmpty(eventFileMap)){
            fileMap = fileService.fileUploadByte(Util.objToStr(eventFileMap.get("fileName")), Util.objToStr(eventFileMap.get("fileContent")));
            map.put("attatchObid", fileMap.get("obid"));
            fileList.add(fileMap);
        }
        validationEventCheck(map);
        eventDao.insertEvent(map);
        fileDao.insertFile(fileList);
    }    

    public void insertParticipate(Map<String, Object> map) throws Exception{
        Map<String,Object> fileMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        Map<String,Object> eventFileMap = Util.objToMap(map.get("files"));
        if(!Util.isEmpty(eventFileMap)){
            fileMap = fileService.fileUploadByte(Util.objToStr(eventFileMap.get("fileName")), Util.objToStr(eventFileMap.get("fileContent")));
            map.put("attatchObid", fileMap.get("obid"));
            fileList.add(fileMap);
        }
        validationParticipateCheck(map);
        eventDao.insertParticipate(map);
        fileDao.insertFile(fileList);
    }

    public int updateEventList(Map<String, Object> map) throws Exception{
        Map<String,Object> fileMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        Map<String,Object> orgEvent = eventDao.selectEvent(map);
        Map<String,Object> eventFileMap = Util.objToMap(map.get("files"));
        if(Util.isEmpty(orgEvent)) throw new RuntimeException("일치하는 정보가 없습니다."); 
        if(!Util.isEmpty(eventFileMap)){
            if(!Util.objToStr(orgEvent.get("fileName")).equals(Util.objToStr(eventFileMap.get("fileName")))){
                fileMap = fileService.fileUploadByte(Util.objToStr(eventFileMap.get("fileName")), Util.objToStr(eventFileMap.get("fileContent")));
                map.put("attatchObid", fileMap.get("obid"));
                fileList.add(fileMap);
            }else{
                map.put("attatchObid", orgEvent.get("attatchObid"));
            }
        }
        validationEventCheck(map);
        int i = eventDao.updateEvent(map);
        fileDao.insertFile(fileList);
        return i;
    }    

    public int updateParticipateList(Map<String, Object> map) throws Exception{
        Map<String,Object> fileMap = new HashMap<String,Object>();
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        Map<String,Object> orgEvent = eventDao.selectParticipate(map);
        Map<String,Object> eventFileMap = Util.objToMap(map.get("files"));
        if(!Util.isEmpty(eventFileMap)){
            if(!Util.objToStr(orgEvent.get("fileName")).equals(Util.objToStr(eventFileMap.get("fileName")))){
                fileMap = fileService.fileUploadByte(Util.objToStr(eventFileMap.get("fileName")), Util.objToStr(eventFileMap.get("fileContent")));
                map.put("attatchObid", fileMap.get("obid"));
                fileList.add(fileMap);
            }else{
                map.put("attatchObid", orgEvent.get("attatchObid"));
            }
        }
        validationParticipateCheck(map);
        int i = eventDao.updateParticipate(map);
        fileDao.insertFile(fileList);
        return i;
    }

    public void validationEventCheck(Map<String,Object> map){
        String name = "행사등록";
        if(Util.isEmpty(map.get("refObid"))){
            throw new RuntimeException(name +" refObid 정보가 없습니다.");
        }
        if(Util.isEmpty(map.get("title"))){
            throw new RuntimeException(name +" 행사타이틀 정보를 입력해 주세요.");
        }
        if(Util.isEmpty(map.get("enTitle"))){
            throw new RuntimeException(name +" 영문타이틀 정보를 입력해 주세요.");
        }
        if(Util.isEmpty(map.get("alias"))){
            throw new RuntimeException(name +" 약칭 정보를 입력해 주세요.");
        }
        if(Util.isEmpty(map.get("objects"))){
            throw new RuntimeException(name +" 행사대상 정보를 입력해 주세요.");
        }
        if(Util.isEmpty(map.get("contents"))){
            throw new RuntimeException(name +" 행사내용 입력해 주세요.");
        }
        if(Util.isEmpty(map.get("products"))){
            throw new RuntimeException(name +" 전시품목 정보를 입력해 주세요.");
        }
        if(Util.isEmpty(map.get("organizer"))){
            throw new RuntimeException(name +" 주최 정보를 입력해 주세요.");
        }
        if(Util.isEmpty(map.get("subjectivity"))){
            throw new RuntimeException(name +" 주관 정보를 입력해 주세요.");
        }
        if(Util.isEmpty(map.get("support"))){
            throw new RuntimeException(name +" 후원 정보를 입력해 주세요.");
        }
        if(Util.isEmpty(map.get("hompage"))){
            throw new RuntimeException(name +" 홈페이지 정보를 입력해 주세요.");
        }
    }   

    public void validationParticipateCheck(Map<String,Object> map){
        String name = "참가모집";
        if(Util.isEmpty(map.get("refObid"))){
            throw new RuntimeException(name +" refObid 정보가 없습니다.");
        }
        if(Util.isEmpty(map.get("objectFor"))){
            throw new RuntimeException(name +" 참가대상 정보를 입력해 주세요.");
        }
        if(Util.isEmpty(map.get("qualifyApplyObjects"))){
            throw new RuntimeException(name +" 신청자격 정보를 입력해 주세요.");
        }
        if(Util.isEmpty(map.get("startObjectsDate"))){
            throw new RuntimeException(name +" 신청기간 시작일 정보를 입력해 주세요.");
        }
        if(Util.isEmpty(map.get("endObjectsDate"))){
            throw new RuntimeException(name +" 신청기가 종료일 정보를 입력해 주세요.");
        }
        if(Util.isEmpty(map.get("supportContent"))){
            throw new RuntimeException(name +" 지원내용 정보를 입력해 주세요.");
        }
        if(Util.isEmpty(map.get("helper"))){
            throw new RuntimeException(name +" 헬퍼모집 정보를 입력해 주세요.");
        }
        if(!Util.dateCheck(map.get("startObjectsDate"))){
            throw new RuntimeException(name +" 신청기간 시작일 날짜 형식이 맞지 않습니다.");
        }
        if(!Util.dateCheck(map.get("endObjectsDate"))){
            throw new RuntimeException(name +" 신청기간 종료일 날짜 형식이 맞지 않습니다.");
        }
        if(!Util.isEmpty(map.get("startHelperDate"))){
            if(!Util.dateCheck(map.get("startHelperDate"))){
                throw new RuntimeException(name +" 헬퍼모집 시작일 날짜 형식이 맞지 않습니다.");
            }
        }
        if(!Util.isEmpty(map.get("endHelperDate"))){
            if(!Util.dateCheck(map.get("endHelperDate"))){
                throw new RuntimeException(name +" 헬퍼모집 종료일 날짜 형식이 맞지 않습니다.");
            }
        }
    }
}
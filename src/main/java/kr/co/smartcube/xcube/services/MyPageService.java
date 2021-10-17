package kr.co.smartcube.xcube.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import kr.co.smartcube.xcube.mybatis.dao.EventCompanyDao;
import kr.co.smartcube.xcube.mybatis.dao.EventDao;
import kr.co.smartcube.xcube.mybatis.dao.MyPageDao;
import kr.co.smartcube.xcube.mybatis.dao.UserDao;

@Service
public class MyPageService {

    @Autowired 
    private MyPageDao myPageDao;

    @Autowired
    private UserDao userDao;

    @Autowired 
    private EventCompanyDao eventCompanyDao;
        
    @Transactional
    public Map<String, Object> selectMyEvent(Map<String,Object> map) {
        Map<String, Object> resultMap = new HashMap<String,Object>();

        Map<String, Object> userMap = userDao.selectUser(map);
        if(!ObjectUtils.isEmpty(userMap)){
            map.put("eventType", "pre");
            List<Map<String,Object>> preEventList = myPageDao.selectMyEventList(map);
            map.put("eventType", "ing");
            List<Map<String,Object>> ingEventList = myPageDao.selectMyEventList(map);
            resultMap.put("preEventList",preEventList);
            resultMap.put("ingEventList",ingEventList);
            //if(!ObjectUtils.isEmpty(preEventList)) resultMap.put("preEventList",preEventList);
            //if(!ObjectUtils.isEmpty(ingEventList)) resultMap.put("ingEventList",ingEventList);
        }else{
            throw new RuntimeException("일치하는 정보가 없습니다.");
        }
        return resultMap;
    }

    @Transactional
    public List<Map<String, Object>> userLicenseSearch(Map<String,Object> map) {
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        Map<String, Object> userMap = userDao.selectUser(map);
        if(!ObjectUtils.isEmpty(userMap)){
            resultList = eventCompanyDao.selectMasterLicenseList(map);
        }else{
            throw new RuntimeException("일치하는 정보가 없습니다.");
        }
        return resultList;
    }

    @Transactional
    public List<Map<String, Object>> participateCompanyHist(Map<String,Object> map) {
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        Map<String, Object> userMap = userDao.selectUser(map);
        if(!ObjectUtils.isEmpty(userMap)){
            resultList = myPageDao.participateCompanyHist(map);
        }else{
            throw new RuntimeException("일치하는 정보가 없습니다.");
        }
        return resultList;
    }

    @Transactional
    public List<Map<String, Object>> participateUserHist(Map<String,Object> map) {
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        Map<String, Object> userMap = userDao.selectUser(map);
        if(!ObjectUtils.isEmpty(userMap)){
            resultList = myPageDao.participateUserHist(map);
        }else{
            throw new RuntimeException("일치하는 정보가 없습니다.");
        }
        return resultList;
    }

    @Transactional
    public void insertParticipateUser(Map<String, Object> map) throws Exception{
        Map<String, Object> userMap = userDao.selectUser(map);
        System.out.println(map);
        if(!ObjectUtils.isEmpty(userMap)){
            myPageDao.insertParticipateUser(map);
        }else{
            throw new RuntimeException("일치하는 정보가 없습니다.");
        }
    }
}
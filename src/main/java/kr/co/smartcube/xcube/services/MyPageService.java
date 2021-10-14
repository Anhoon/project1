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
        map.put("type", "pre");
        List<Map<String,Object>> preEventList = myPageDao.selectMyEventList(map);
        map.put("type", "ing");
        List<Map<String,Object>> ingEventList = myPageDao.selectMyEventList(map);
        resultMap.put("preEventList",preEventList);
        resultMap.put("ingEventList",ingEventList);
        //if(!ObjectUtils.isEmpty(preEventList)) resultMap.put("preEventList",preEventList);
        //if(!ObjectUtils.isEmpty(ingEventList)) resultMap.put("ingEventList",ingEventList);
        return resultMap;
    }

    public List<Map<String, Object>> selectMasterLicenseList(Map<String,Object> map) {
        return eventCompanyDao.selectMasterLicenseList(map);
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
    
}
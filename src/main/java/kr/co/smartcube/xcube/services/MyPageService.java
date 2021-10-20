package kr.co.smartcube.xcube.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import kr.co.smartcube.xcube.mybatis.dao.MasterDao;
import kr.co.smartcube.xcube.mybatis.dao.MyPageDao;
import kr.co.smartcube.xcube.mybatis.dao.UserDao;
import kr.co.smartcube.xcube.util.Util;

@Service
public class MyPageService {

    @Autowired 
    private MyPageDao myPageDao;

    @Autowired
    private UserDao userDao;

    @Autowired 
    private MasterDao eventCompanyDao;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
        
    @Transactional
    public Map<String, Object> selectMyEvent(Map<String,Object> map) {
        Map<String, Object> resultMap = new HashMap<String,Object>();

        Map<String, Object> userMap = userDao.selectUser(map);
        if(!ObjectUtils.isEmpty(userMap)){
            map.put("status", "1");
            List<Map<String,Object>> preEventList = myPageDao.selectMyEventList(map);
            map.put("status", "-1");
            List<Map<String,Object>> ingEventList = myPageDao.selectMyEventList(map);
            resultMap.put("preEventList",preEventList);
            resultMap.put("ingEventList",ingEventList);
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
            map.put("status", "1");
            resultList = eventCompanyDao.selectMasterLicenseList(map);
        }else{
            throw new RuntimeException("일치하는 정보가 없습니다.");
        }
        return resultList;
    }

    @Transactional
    public PageInfo<Map<String, Object>> selectParticipateApplyHist(Map<String,Object> map) {
        Map<String, Object> userMap = userDao.selectUser(map);
        if(!ObjectUtils.isEmpty(userMap)){
            PageHelper.startPage((int)map.get("pageNum"), (int)map.get("pageSize"), (String) map.get("orderBy"));
            return myPageDao.selectParticipateApplyHist(map);
        }else{
            throw new RuntimeException("일치하는 정보가 없습니다.");
        }
    }

    @Transactional
	public Map<String, Object> selectParticipateApplyHistDetail(Map<String,Object> map) {
        Map<String, Object> resultMap = myPageDao.selectParticipateApplyHistDetail(map);
        if(!ObjectUtils.isEmpty(resultMap)){
            if(!ObjectUtils.isEmpty(resultMap.get("businessType"))){
                resultMap.put("business_type", Util.jsonToArray(Util.objToStr(resultMap.get("businessType"))));
            }
        }else{
            throw new RuntimeException("일치하는 정보가 없습니다.");
        }
		return resultMap;
    }


    @Transactional
    public PageInfo<Map<String, Object>> selectParticipateAuthHist(Map<String,Object> map) {
        Map<String, Object> userMap = userDao.selectUser(map);
        if(!ObjectUtils.isEmpty(userMap)){
            PageHelper.startPage((int)map.get("pageNum"), (int)map.get("pageSize"), (String) map.get("orderBy"));
            return myPageDao.selectParticipateAuthHist(map);
        }else{
            throw new RuntimeException("일치하는 정보가 없습니다.");
        }
    }

    @Transactional
    public void insertParticipateAuth(Map<String, Object> map) throws Exception{
        Map<String, Object> userMap = userDao.selectUser(map);
        if(!ObjectUtils.isEmpty(userMap)){
            myPageDao.insertParticipateAuth(map);
        }else{
            throw new RuntimeException("일치하는 정보가 없습니다.");
        }
    }

    @Transactional
    public Map<String,Object> userPasswordCheck(Map<String,Object> map) throws Exception{
        Map<String, Object> resultMap = userDao.selectUser(map);
        if(ObjectUtils.isEmpty(resultMap)) throw new RuntimeException("일치하는 회원정보가 없습니다.");
        boolean pwdChk = passwordEncoder.matches(Util.objToStr(map.get("password")), Util.objToStr(resultMap.get("password")));
        if(!pwdChk) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다. 비밀번호를 확인해주세요");
        }
        return resultMap;
    }
}
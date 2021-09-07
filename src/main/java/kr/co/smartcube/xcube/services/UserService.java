package kr.co.smartcube.xcube.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.smartcube.xcube.exception.XcubeException;
import kr.co.smartcube.xcube.mybatis.dao.UserDao;
import kr.co.smartcube.xcube.util.Util;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Map<String, Object>> selectUserList() {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        try {
            resultList = userDao.selectUserList();    
        } catch (Exception e) {
            throw new XcubeException(e.toString());
        }
		return resultList;
    }

    public Map<String, Object> selectUser(Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            resultMap = userDao.selectUser(map);
        } catch (Exception e) {
            throw new XcubeException(e.toString());
        }
		return resultMap;
    }
    
    @Transactional
    public void insertUser(Map<String, Object> map){
        try {
            String password = Util.objToStr(map.get("password"));
            String userType = Util.objToStr(map.get("userType"));
            if(Util.isEmpty(userType)){
                throw new XcubeException("userTpye Is Null");    
            }
            String obid = UUID.randomUUID().toString();
            map.put("obid", obid);
            map.put("password", passwordEncoder.encode(password));
            map.put("userStatus", "0");
            if(!Util.isEmpty(map.get("businessType"))) map.put("businessType",Util.objToJson(map.get("businessType")));
            int i = userType.equals("0") ? userDao.insertUser(map) : userType.equals("1") || userType.equals("2") ? userDao.insertUserCompany(map) : 0;
            if(i > 0) userDao.insertUserHistory(getHistMap(map,"I",map)); //이력 저장
            emailService.sendMail(map); //메일전송
        } catch (DuplicateKeyException e) {
            throw new XcubeException("PK 중복 에러");
        } catch (Exception e){
            throw new XcubeException(e.toString());
        }
    }

    @Transactional
    public void updateUser(Map<String, Object> map){
        try {
            Map<String, Object> resultList = userDao.selectUser(map); //현재 등록된 
            Map<String, Object> detailMap = new HashMap<String,Object>(); //이력 Detail컬럼 Map
            if(!Util.isEmpty(resultList)){
                String password = Util.objToStr(map.get("password"));
                String userType = Util.objToStr(resultList.get("userType"));
                boolean pwdChk = passwordEncoder.matches(password, Util.objToStr(resultList.get("password")));
                if(!pwdChk) {
                    map.put("password", passwordEncoder.encode(password));
                    detailMap.put("password", map.get("password"));
                }else{
                    map.put("password", resultList.get("password"));
                }
                if(!Util.isEmpty(map.get("businessType"))) map.put("businessType",Util.objToJson(map.get("businessType")));
                int i = userType.equals("0") ? userDao.updateUser(map) : userType.equals("1") || userType.equals("2") ? userDao.updateUserCompany(map) : 0;
                if(i > 0) {
                    for(String key : map.keySet()){
                        if(!Util.objToStr(map.get(key)).equals(Util.objToStr(resultList.get(key)))){
                            if(!key.equals("password")) detailMap.put(key, map.get(key));
                        }
                    }
                    userDao.insertUserHistory(getHistMap(map,"U",detailMap)); //이력 저장
                }
            }else{
                throw new XcubeException("등록된 회원이 없습니다.");
            }
        } catch (Exception e){
            throw new XcubeException(e.toString());
        }   
    }

    @Transactional
    public void userCertified(Map<String, Object> map){
        try {
            Map<String,Object> userMap = userDao.selectUser(map);
            if(!Util.isEmpty(userMap)) {
                if(!Util.objToStr(userMap.get("userStatus")).equals("0")) throw new XcubeException("이미 인증된 회원입니다.");
                map.put("userStatus", "1"); //정상가입
                int i = userDao.updateUserStatus(map);
                if(i > 0) {
                    Map<String, Object> detailMap = new HashMap<String,Object>(); //이력 Detail컬럼 Map
                    detailMap.put("userStatus", "1");
                    userDao.insertUserHistory(getHistMap(map,"U",detailMap)); //이력 저장
                }
            }else{
                throw new XcubeException("일치하는 회원이 없습니다.");
            }
        } catch (Exception e) {
            throw new XcubeException(e.toString());
        }
    }

    @Transactional
    public void deleteUserStatus(Map<String, Object> map){
        try {
            map.put("userStatus", "-1"); //탈퇴회원
            int i = userDao.updateUserStatus(map);
            if(i > 0) {
                Map<String, Object> detailMap = new HashMap<String,Object>(); //이력 Detail컬럼 Map
                detailMap.put("userStatus", "-1");
                userDao.insertUserHistory(getHistMap(map,"D",detailMap)); //이력 저장
            }    
        } catch (Exception e) {
            throw new XcubeException(e.toString());
        }
	}

    @Transactional
    public void insertUserHistory(Map<String, Object> map){
        try {
            userDao.insertUserHistory(map);
        } catch (Exception e) {
            throw new XcubeException(e.toString());
        }
    }

    //이력 저장 맵 생성
    public Map<String,Object> getHistMap(Map<String,Object> map,String type, Map<String, Object> detailMap){
        String hisObid = UUID.randomUUID().toString();
        Map<String,Object> histMap = new HashMap<String,Object>();
        histMap.put("refObid", (String) map.get("obid"));
        histMap.put("obid", hisObid);
        histMap.put("reqType", type);
        if(!Util.isEmpty(detailMap)) histMap.put("detail", Util.objToJson(detailMap));
        return histMap;
    }

}
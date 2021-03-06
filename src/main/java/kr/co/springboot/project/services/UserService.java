package kr.co.springboot.project.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import kr.co.springboot.project.mybatis.dao.UserDao;
import kr.co.springboot.project.util.Util;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PageInfo<Map<String, Object>> selectUserList(Map<String,Object> map) {
        PageHelper.startPage((int)map.get("pageNum"), (int)map.get("pageSize"), (String) map.get("orderBy"));
        return userDao.selectUserList(map);
    }

    public Map<String, Object> selectUser(Map<String, Object> map) throws Exception{
        Map<String, Object> resultMap = userDao.selectUser(map);
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
    public void insertUser(Map<String, Object> map) throws Exception{
        String password = Util.objToStr(map.get("password"));
        String userType = Util.objToStr(map.get("userType"));
        String obid = UUID.randomUUID().toString();
        map.put("obid", obid);
        map.put("password", passwordEncoder.encode(password));
        map.put("userStatus", "0");
        if(!ObjectUtils.isEmpty(map.get("businessType"))) map.put("businessType",Util.objToJson(map.get("businessType")));
        validationUserCheck(map);
        int i = userType.equals("0") ? userDao.insertUser(map) : userType.equals("1") || userType.equals("2") ? userDao.insertUserCompany(map) : 0;
        if(i > 0) userDao.insertUserHistory(getHistMap(map,"I",map)); //이력 저장
        emailService.sendMail(map); //메일전송
    }

    @Transactional
    public void updateUser(Map<String, Object> map) throws Exception{  
        Map<String, Object> resultMap = userDao.selectUser(map); //현재 등록된 
        Map<String, Object> detailMap = new HashMap<String,Object>(); //이력 Detail컬럼 Map
        if(!ObjectUtils.isEmpty(resultMap)){
            String password = Util.objToStr(map.get("password"));
            String userType = Util.objToStr(resultMap.get("userType"));
            boolean pwdChk = passwordEncoder.matches(password, Util.objToStr(resultMap.get("password")));
            if(!pwdChk) {
                map.put("password", passwordEncoder.encode(password));
                detailMap.put("password", map.get("password"));
            }else{
                map.put("password", resultMap.get("password"));
            }
            if(!ObjectUtils.isEmpty(map.get("businessType"))) map.put("businessType",Util.objToJson(map.get("businessType")));
            int i = userType.equals("0") ? userDao.updateUser(map) : userType.equals("1") || userType.equals("2") ? userDao.updateUserCompany(map) : 0;
            if(i > 0) {
                for(String key : map.keySet()){
                    if(!Util.objToStr(map.get(key)).equals(Util.objToStr(resultMap.get(key)))){
                        if(!key.equals("password")) detailMap.put(key, map.get(key));
                    }
                }
                userDao.insertUserHistory(getHistMap(map,"U",detailMap)); //이력 저장
            }else{
                throw new RuntimeException("일치하는 정보가 없습니다.");
            }
        }else{
            throw new RuntimeException("일치하는 정보가 없습니다.");
        }
    }

    @Transactional
    public void userCertified(Map<String, Object> map) throws Exception{
        Map<String,Object> userMap = userDao.selectUser(map);
        if(!ObjectUtils.isEmpty(userMap)) {
            //if(!Util.objToStr(userMap.get("userStatus")).equals("0")) throw new RuntimeException("이미 인증된 회원입니다.");
            map.put("userStatus", "1"); //정상가입
            int i = userDao.updateUserStatus(map);
            if(i > 0) {
                Map<String, Object> detailMap = new HashMap<String,Object>(); //이력 Detail컬럼 Map
                detailMap.put("userStatus", "1");
                userDao.insertUserHistory(getHistMap(map,"U",detailMap)); //이력 저장
            }else{
                //throw new RuntimeException("일치하는 정보가 없습니다.");    
            }
        }else{
            //throw new RuntimeException("일치하는 정보가 없습니다.");
        }
    }

    @Transactional
    public void deleteUserStatus(Map<String, Object> map) throws Exception{
        map.put("userStatus", "-1"); //탈퇴회원
        int i = userDao.updateUserStatus(map);
        if(i > 0) {
            Map<String, Object> detailMap = new HashMap<String,Object>(); //이력 Detail컬럼 Map
            detailMap.put("userStatus", "-1");
            userDao.insertUserHistory(getHistMap(map,"D",detailMap)); //이력 저장
        }else{
            throw new RuntimeException("일치하는 정보가 없습니다.");
        }
	}

    @Transactional
    public void findPassword(String email) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String password = UUID.randomUUID().toString();
        map.put("email", email);
        map.put("password", password);
        emailService.sendPassWordMail(map); //메일전송
        map.put("password", passwordEncoder.encode(password));
        userDao.updateUserPassword(map);
    }

    @Transactional
    public void userCheck(Map<String,Object> map) throws Exception{
        Map<String, Object> resultMap = userDao.selectUser(map);
        if(!ObjectUtils.isEmpty(resultMap)) throw new RuntimeException("이미 존재하는 회원입니다.");
    }
        
    //이력 저장 맵 생성
    public Map<String,Object> getHistMap(Map<String,Object> map,String type, Map<String, Object> detailMap){
        String hisObid = UUID.randomUUID().toString();
        Map<String,Object> histMap = new HashMap<String,Object>();
        histMap.put("usersObid", Util.objToStr(map.get("obid")));
        histMap.put("obid", hisObid);
        histMap.put("reqType", type);
        if(!ObjectUtils.isEmpty(detailMap)) histMap.put("detail", Util.objToJson(detailMap));
        return histMap;
    }

    public void validationUserCheck(Map<String,Object> map){
        if(ObjectUtils.isEmpty(map.get("obid"))){
            throw new RuntimeException("obid 정보가 없습니다.");
        }
        if(ObjectUtils.isEmpty(map.get("email"))){
            throw new RuntimeException("이메일 정보를 입력해 주세요.");
        }
        if(ObjectUtils.isEmpty(map.get("password"))){
            throw new RuntimeException("패스워드 정보를 입력해 주세요.");
        }
        if(ObjectUtils.isEmpty(map.get("name"))){
            throw new RuntimeException("닉네임 정보를 입력해 주세요.");
        }
        if(ObjectUtils.isEmpty(map.get("userType"))){
            throw new RuntimeException("유저상태 정보를 입력해 주세요.");
        }
    }  

}
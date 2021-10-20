package kr.co.smartcube.xcube.services;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import kr.co.smartcube.xcube.mybatis.dao.AuthDao;
import kr.co.smartcube.xcube.util.Util;

@Service
public class AuthService {

    @Autowired
    private AuthDao authDao;

    /*권한관리*/
    public PageInfo<Map<String, Object>> selectAuthManageList(Map<String,Object> paramMap) throws Exception {
        PageHelper.startPage((int)paramMap.get("pageNum"), (int)paramMap.get("pageSize"), (String) paramMap.get("orderBy"));
        return authDao.selectAuthManageList(paramMap);
    }

    public Map<String, Object> selectAuthManage(Map<String,Object> paramMap) throws Exception {
        validateAuth(paramMap, "S");
        Map<String,Object> authManageMap = authDao.selectAuthManage(paramMap);
        if(ObjectUtils.isEmpty(authManageMap)) 
            throw new RuntimeException("일치하는 정보가 없습니다."); 
        return authManageMap;
    }

    @Transactional
    public void insertAuthManage(Map<String, Object> paramMap) throws Exception{
        validateAuth(paramMap, "I");
        authDao.insertAuthManage(paramMap);
    }

    @Transactional
    public void updateAuthManage(Map<String, Object> paramMap) throws Exception{
        validateAuth(paramMap, "U");

        Map<String,Object> authManageMap = authDao.selectAuthManage(paramMap);

        if(ObjectUtils.isEmpty(authManageMap))
            throw new RuntimeException("일치하는 정보가 없습니다."); 

        authDao.updateAuthManage(paramMap);
    }

    @Transactional
    public void updateAuthManageAuthList(Map<String, Object> paramMap) throws Exception{
        validateAuth(paramMap, "UL");

        List<Map<String,Object>> authManageList = Util.objToList(paramMap.get("list"));
    
        for(Map<String,Object> authManageMap : authManageList){
            if(!ObjectUtils.isEmpty(authManageMap) && !ObjectUtils.isEmpty(authManageMap.get("masterLicenseListObid")) && 
               !ObjectUtils.isEmpty(authManageMap.get("email")) && !ObjectUtils.isEmpty(authManageMap.get("grade")))
                authDao.updateAuthManageAuth(authManageMap);
        }
    }

    @Transactional
    public void deleteAuthManage(Map<String, Object> paramMap) throws Exception{
        validateAuth(paramMap, "D");

        Map<String,Object> authManageMap = authDao.selectAuthManage(paramMap);
        if(ObjectUtils.isEmpty(authManageMap)) throw new RuntimeException("일치하는 정보가 없습니다.");

        authDao.deleteAuthManage(authManageMap);
    }

    @Transactional
    public void deleteAuthManageList(Map<String, Object> paramMap) throws Exception{
        validateAuth(paramMap, "DL");

        List<Map<String,Object>> authManageList = Util.objToList(paramMap.get("list"));
        
        for(Map<String,Object> authManageMap : authManageList){
            if(!ObjectUtils.isEmpty(authManageMap) && 
               !ObjectUtils.isEmpty(authManageMap.get("masterLicenseListObid")) && 
               !ObjectUtils.isEmpty(authManageMap.get("email")))
                authDao.deleteAuthManage(authManageMap);
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
}
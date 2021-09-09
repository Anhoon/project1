package kr.co.smartcube.xcube.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smartcube.xcube.services.UserService;
import kr.co.smartcube.xcube.util.Util;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public List<Map<String, Object>> selectUserList(){
        List<Map<String, Object>> resultList = userService.selectUserList();
        return resultList; 
    }

    @GetMapping("/{email}")
    public Map<String, Object> selectUser(@PathVariable String email) throws Exception {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("email", email);
        Map<String, Object> resultMap = userService.selectUser(paramMap);
        if(!Util.isEmpty(resultMap) && !Util.isEmpty(resultMap.get("businessType"))){
            resultMap.put("businessType", Util.jsonToArray((String) resultMap.get("businessType")));
        }
        return resultMap; 
    }

    @PostMapping("/")
    public Map<String, Object> insertData(@RequestBody Map<String, Object> map){
        userService.insertUser(map); //등록
        return Util.getResult(true);
    }

    @PatchMapping("/")
    public Map<String, Object> updateData(@RequestBody Map<String, Object> map){
        userService.updateUser(map);
        return Util.getResult(true);
    }

    @DeleteMapping("/")
    public Map<String, Object> deleteData(@RequestBody Map<String, Object> map){
        userService.deleteUserStatus(map);
        return Util.getResult(true);
    }

    @GetMapping(value = "/certified")
    public Map<String, Object> mailCheck(@RequestParam(value = "obid") String obid, @RequestParam(value = "email") String email){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("obid", obid);
        paramMap.put("email", email);
        userService.userCertified(paramMap);
        return Util.getResult(true);
    }

}
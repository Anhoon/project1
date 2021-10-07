package kr.co.smartcube.xcube.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import kr.co.smartcube.xcube.common.CommonResult;
import kr.co.smartcube.xcube.common.ResponseService;
import kr.co.smartcube.xcube.services.UserService;
import kr.co.smartcube.xcube.util.Util;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseService responseService;

    @GetMapping("/")
    public ResponseEntity<CommonResult> selectUserList() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pageNum", 0);
        map.put("pageSize", 2);
        map.put("orderBy", "1 desc");
        map = Util.initPaginagtion(map);
        try {
            return new ResponseEntity<CommonResult>(responseService.getListResult(userService.selectUserList(map)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<CommonResult> selectUser(@PathVariable String email) throws Exception {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("email", email);
        try {
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(userService.selectUser(paramMap)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/")
    public ResponseEntity<CommonResult> insertData(@RequestBody Map<String, Object> map) throws Exception{
        try {
            userService.insertUser(map);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
    }

    @PatchMapping("/")
    public ResponseEntity<CommonResult> updateData(@RequestBody Map<String, Object> map) throws Exception{
        try {
            userService.updateUser(map);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
    }

    @DeleteMapping("/")
    public ResponseEntity<CommonResult> deleteData(@RequestBody Map<String, Object> map) throws Exception{
        try {
            userService.deleteUserStatus(map);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
    }

    @GetMapping(value = "/certified")
    public ResponseEntity<CommonResult> mailCheck(@RequestParam(value = "obid") String obid, @RequestParam(value = "email") String email) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("obid", obid);
        map.put("email", email);
        try {
            userService.userCertified(map);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
        /*
        URI redirectUri = new URI("http://www.naver.com");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        */
        return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
    }

    @GetMapping("/findPassword/{email}")
    public ResponseEntity<CommonResult> findPassword(@PathVariable String email) throws Exception{
        try {
            userService.findPassword(email);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
    }

}
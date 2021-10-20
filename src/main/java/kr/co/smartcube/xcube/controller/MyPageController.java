package kr.co.smartcube.xcube.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import kr.co.smartcube.xcube.services.EventService;
import kr.co.smartcube.xcube.services.MyPageService;
import kr.co.smartcube.xcube.services.UserService;
import kr.co.smartcube.xcube.util.Util;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/myPage")
public class MyPageController {

    @Autowired
    private MyPageService myPageService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ResponseService responseService;

    @PatchMapping("/updateUserInfo")
    public ResponseEntity<CommonResult> updateUserInfo(@RequestBody Map<String, Object> map) throws Exception{
        try {
            userService.updateUser(map);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/myEventInfo/{email}")
    public ResponseEntity<CommonResult> selectMyEventInfoCompany(@PathVariable String email) throws Exception 
    {
        try {
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("email", email);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(myPageService.selectMyEvent(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/myEventDetailInfo/{masterLicenseListObid}")
    public ResponseEntity<CommonResult> selectMasterLicenseListDetailList(@PathVariable String masterLicenseListObid) throws Exception {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("masterLicenseListObid", masterLicenseListObid);
        try {
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(eventService.selectEvent(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/participateApplyHist/{email}")
    public ResponseEntity<CommonResult> selectParticipateApplyHist(
            @PathVariable String email,
            @RequestParam(required = false) String searchKey,
            @RequestParam(required = false) String searchKeyWord,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderBy) throws Exception 
    {
        try {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("email", email);
            if(!ObjectUtils.isEmpty(searchKey)) map.put("searchKey", searchKey);
            if(!ObjectUtils.isEmpty(searchKeyWord)) map.put("searchKeyWord", searchKeyWord);
            map.put("pageNum", pageNum);
            map.put("pageSize", pageSize);
            map.put("orderBy", orderBy);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(myPageService.selectParticipateApplyHist(Util.initPaginagtion(map))), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/participateApplyDetail")
    public ResponseEntity<CommonResult> selectParticipateApplyHistDetail(
            @RequestParam String email,
            @RequestParam String masterLicenseListObid) throws Exception
    {
        try {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("email", email);
            map.put("masterLicenseListObid", masterLicenseListObid);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(myPageService.selectParticipateApplyHistDetail(map)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }
    

    @GetMapping("/participateAuthHist/{email}")
    public ResponseEntity<CommonResult> selectParticipateAuthHist(
            @PathVariable String email,
            @RequestParam(required = false) String searchKey,
            @RequestParam(required = false) String searchKeyWord,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderBy) throws Exception
    {
        try {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("email", email);
            if(!ObjectUtils.isEmpty(searchKey)) map.put("searchKey", searchKey);
            if(!ObjectUtils.isEmpty(searchKeyWord)) map.put("searchKeyWord", searchKeyWord);
            map.put("pageNum", pageNum);
            map.put("pageSize", pageSize);
            map.put("orderBy", orderBy);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(myPageService.selectParticipateAuthHist(Util.initPaginagtion(map))), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }
    
    @PostMapping("/insertParticipateAuth/{email}")
    public ResponseEntity<CommonResult> insertParticipateAuth(@PathVariable String email,@RequestBody Map<String, Object> map) throws Exception
    {
        try {
            map.put("email", email);
            myPageService.insertParticipateAuth(map);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/userLicenseSearch/{email}")
    public ResponseEntity<CommonResult> userLicenseSearch(@PathVariable String email) throws Exception 
    {
        try {
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("email", email);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(myPageService.userLicenseSearch(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/userPasswordCheck")
    public ResponseEntity<CommonResult> userPasswordCheck(@RequestParam(value = "password") String password, @RequestParam(value = "email") String email) throws Exception {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("email", email);
        paramMap.put("password", password);
        try {
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(myPageService.userPasswordCheck(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }
}
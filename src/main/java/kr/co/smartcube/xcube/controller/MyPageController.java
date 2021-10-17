package kr.co.smartcube.xcube.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smartcube.xcube.common.CommonResult;
import kr.co.smartcube.xcube.common.ResponseService;
import kr.co.smartcube.xcube.services.EventService;
import kr.co.smartcube.xcube.services.MyPageService;
import kr.co.smartcube.xcube.services.UserService;
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

    @GetMapping("/participateCompanyHist/{email}")
    public ResponseEntity<CommonResult> selectParticipateCompanyHist(@PathVariable String email) throws Exception 
    {
        try {
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("email", email);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(myPageService.participateCompanyHist(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/participateUserHist/{email}")
    public ResponseEntity<CommonResult> selectParticipateUserHist(@PathVariable String email) throws Exception 
    {
        try {
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("email", email);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(myPageService.participateUserHist(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    } 
    
    @PostMapping("/insertParticipateUser/{email}")
    public ResponseEntity<CommonResult> participateUserInsert(@PathVariable String email,@RequestBody Map<String, Object> map) throws Exception
    {
        try {
            map.put("email", email);
            myPageService.insertParticipateUser(map);
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
}
package kr.co.smartcube.xcube.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import kr.co.smartcube.xcube.common.CommonResult;
import kr.co.smartcube.xcube.common.ResponseService;
import kr.co.smartcube.xcube.services.ManageService;
import kr.co.smartcube.xcube.util.Util;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/manage")
public class ManageController {

    @Autowired
    private ManageService manageService;
    
    @Autowired
    private ResponseService responseService;

    /*참가기업*/
    @GetMapping("/company/")
    public ResponseEntity<CommonResult> selectJoinCompanyList(){
        try {
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(manageService.selectJoinCompanyList(Util.initPaginagtion())), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/company/{refObid}")
    public ResponseEntity<CommonResult> selectJoinCompany(@PathVariable String refObid) throws Exception {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("refObid", refObid);
        try {
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(manageService.selectJoinCompany(paramMap)), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/company/")
    public ResponseEntity<CommonResult> insertJoinCompany(@RequestBody Map<String, Object> map) throws Exception{
        try {
            manageService.insertJoinCompany(map);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/company/{refObid}")
    public ResponseEntity<CommonResult> updateJoinCompany(@PathVariable String refObid, @RequestBody Map<String, Object> map){
        try {
            map.put("refObid", refObid);
            manageService.updateJoinCompany(map);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/company/{refObid}")
    public ResponseEntity<CommonResult> deleteJoinCompany(@PathVariable String refObid){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        try {
            paramMap.put("refObid", refObid);
            manageService.deleteJoinCompany(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*참여인력*/
    @GetMapping("/user/")
    public ResponseEntity<CommonResult> selectJoinUserList(){
        try {
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(manageService.selectJoinUserList(Util.initPaginagtion())), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/user/{refObid}")
    public ResponseEntity<CommonResult> selectJoinUser(@PathVariable String refObid) throws Exception {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("refObid", refObid);
        try {
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(manageService.selectJoinCompany(paramMap)), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/user/")
    public ResponseEntity<CommonResult> insertJoinUser(@RequestBody Map<String, Object> map) throws Exception{
        try {
            manageService.insertJoinUser(map);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/user/{refObid}")
    public ResponseEntity<CommonResult> updateJoinUser(@PathVariable String refObid, @RequestBody Map<String, Object> map){
        try {
            map.put("refObid", refObid);
            manageService.updateJoinUser(map);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/user/{refObid}")
    public ResponseEntity<CommonResult> deleteJoinUser(@PathVariable String refObid){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        try {
            paramMap.put("refObid", refObid);
            manageService.deleteJoinUser(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }
}
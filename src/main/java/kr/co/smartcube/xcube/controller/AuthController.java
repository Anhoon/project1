package kr.co.smartcube.xcube.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.ObjectUtils;
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
import kr.co.smartcube.xcube.common.security.jwt.LoginVO;
import kr.co.smartcube.xcube.services.AuthService;
import kr.co.smartcube.xcube.util.Util;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private ResponseService responseService;

    /*권한관리*/
    /*권한관리 목록*/
    @GetMapping("/list")
    public ResponseEntity<CommonResult> selectAuthManageList(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestParam(required = false) String[] masterLicenseListObid,
        @RequestParam(required = false, defaultValue = "1") Integer pageNum,
        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
        @RequestParam(required = false) String orderBy,
        @RequestParam(required = false) String searchKey,
        @RequestParam(required = false) String searchKeyWord) throws Exception 
    {
        try {
            Map<String, Object> paramMap = new HashMap<String,Object>();
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("pageNum", pageNum);
            paramMap.put("pageSize", pageSize);
            paramMap.put("orderBy", orderBy);
            paramMap.put("searchKey", searchKey);
            paramMap.put("searchKeyWord", searchKeyWord);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(authService.selectAuthManageList(Util.initPaginagtion(paramMap))), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*권한관리 상세정보*/
    @GetMapping("/{masterLicenseListObid}/{email}")
    public ResponseEntity<CommonResult> selectAuthManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String masterLicenseListObid,
        @PathVariable String email) throws Exception 
    {
        try {
            Map<String, Object> paramMap = new HashMap<String,Object>();
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("email", email);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(authService.selectAuthManage(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*권한관리 등록*/
    @PostMapping("/{masterLicenseListObid}/{email}")
    public ResponseEntity<CommonResult> insertAuthManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String masterLicenseListObid,
        @PathVariable String email,
        @RequestBody(required = false) Map<String, Object> paramMap) throws Exception
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("email", email);
            authService.insertAuthManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*권한관리 수정*/
    @PatchMapping("/{masterLicenseListObid}/{email}")
    public ResponseEntity<CommonResult> updateAuthManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String masterLicenseListObid,
        @PathVariable String email,
        @RequestBody(required = false) Map<String, Object> paramMap) throws Exception
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("email", email);
            authService.updateAuthManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*권한관리 권한저장 다건*/
    @PatchMapping("/list")
    public ResponseEntity<CommonResult> updateAuthManageList(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            authService.updateAuthManageAuthList(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*권한관리 삭제 단건*/
    @DeleteMapping("/{masterLicenseListObid}/{email}")
    public ResponseEntity<CommonResult> deleteAuthManage(
        @PathVariable String masterLicenseListObid,
        @PathVariable String email,
        @RequestBody(required = false) Map<String, Object> paramMap) throws Exception
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("email", email);
            authService.deleteAuthManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*권한관리 삭제 다건*/
    @DeleteMapping("/list")
    public ResponseEntity<CommonResult> deleteAuthManageList(
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            authService.deleteAuthManageList(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }
}
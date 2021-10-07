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
import org.springframework.web.bind.annotation.RestController;

import kr.co.smartcube.xcube.common.CommonResult;
import kr.co.smartcube.xcube.common.ResponseService;
import kr.co.smartcube.xcube.common.security.jwt.LoginVO;
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
    @GetMapping("/company")
    public ResponseEntity<CommonResult> selectJoinCompanyList(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody(required = false) Map<String, Object> paramMap)
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("email", loginVO.getEmail());
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(manageService.selectJoinCompanyList(Util.initPaginagtion(paramMap))), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/company/{refObidCompany}")
    public ResponseEntity<CommonResult> selectJoinCompany(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String refObidCompany,
        @RequestBody Map<String, Object> paramMap) throws Exception 
    {
        try {
            paramMap.put("refObidCompany", refObidCompany);
            paramMap.put("email", loginVO.getEmail());
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(manageService.selectJoinCompany(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/company")
    public ResponseEntity<CommonResult> insertJoinCompany(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            paramMap.put("email", loginVO.getEmail());
            manageService.insertJoinCompany(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/company")
    public ResponseEntity<CommonResult> updateJoinCompany(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody Map<String, Object> paramMap)
    {
        try {
            paramMap.put("email", loginVO.getEmail());
            manageService.updateJoinCompany(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/company")
    public ResponseEntity<CommonResult> deleteJoinCompany(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody Map<String, Object> paramMap)
    {
        try {
            paramMap.put("email", loginVO.getEmail());
            manageService.deleteJoinCompany(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*관심기업*/
    @GetMapping("/company/interest")
    public ResponseEntity<CommonResult> selectInterestingCompanyList(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody(required = false) Map<String, Object> paramMap
    ){
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("email", loginVO.getEmail());
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(manageService.selectInterestCompanyList(Util.initPaginagtion(paramMap))), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/company/interest")
    public ResponseEntity<CommonResult> insertInterestingCompany(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody Map<String, Object> paramMap
    ){
        try {
            if(ObjectUtils.isEmpty(paramMap) || ObjectUtils.isEmpty(paramMap.get("refObid")) || ObjectUtils.isEmpty(paramMap.get("refObidCompany"))) 
                return new ResponseEntity<CommonResult>(responseService.getFailResult("행사정보, 기업정보를 확인하시기 바랍니다."), HttpStatus.CONFLICT);

            paramMap.put("email", loginVO.getEmail());
            manageService.insertInterestCompany(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/company/interest")
    public ResponseEntity<CommonResult> deleteInterestingCompany(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody Map<String, Object> paramMap
    ){
        try {
            paramMap.put("email", loginVO.getEmail());
            manageService.deleteInterestCompany(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*참여인력*/
    @GetMapping("/user")
    public ResponseEntity<CommonResult> selectJoinUserList(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody(required = false) Map<String, Object> paramMap) throws Exception 
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("email", loginVO.getEmail());
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(manageService.selectJoinUserList(Util.initPaginagtion(paramMap))), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/user/{refObidUser}")
    public ResponseEntity<CommonResult> selectJoinUser(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String refObidUser,
        @RequestBody Map<String, Object> paramMap) throws Exception 
    {
        try {
            paramMap.put("refObidUser", refObidUser);
            paramMap.put("email", loginVO.getEmail());
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(manageService.selectJoinUser(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/user")
    public ResponseEntity<CommonResult> insertJoinUser(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            paramMap.put("email", loginVO.getEmail());
            manageService.insertJoinUser(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/user")
    public ResponseEntity<CommonResult> updateJoinUser(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody Map<String, Object> paramMap)
    {
        try {
            paramMap.put("email", loginVO.getEmail());
            manageService.updateJoinUser(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/user")
    public ResponseEntity<CommonResult> deleteJoinUser(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody Map<String, Object> paramMap)
    {
        try {
            paramMap.put("email", loginVO.getEmail());
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
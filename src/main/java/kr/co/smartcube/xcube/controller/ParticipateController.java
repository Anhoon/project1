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
import kr.co.smartcube.xcube.services.ParticipateService;
import kr.co.smartcube.xcube.util.Util;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/participate")
public class ParticipateController {

    @Autowired
    private ParticipateService participateService;
    
    @Autowired
    private ResponseService responseService;

    /*참가신청관리*/
    /*참가신청관리 목록*/
    @GetMapping("/apply/list")
    public ResponseEntity<CommonResult> selectParticipateManageList(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestParam(required = false) String[] masterLicenseListObid,
        @RequestParam(required = false, defaultValue = "1") Integer pageNum,
        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
        @RequestParam(required = false) String orderBy,
        @RequestParam(required = false) String searchKey,
        @RequestParam(required = false) String searchKeyWord) throws Exception 
    {
        try {
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("pageNum", pageNum);
            paramMap.put("pageSize", pageSize);
            paramMap.put("orderBy", orderBy);
            paramMap.put("searchKey", searchKey);
            paramMap.put("searchKeyWord", searchKeyWord);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(participateService.selectParticipateManageList(Util.initPaginagtion(paramMap))), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*참가신청관리 상세정보*/
    @GetMapping("/apply/{masterLicenseListObid}/{email}")
    public ResponseEntity<CommonResult> selectParticipateManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String masterLicenseListObid,
        @PathVariable String email) throws Exception 
    {
        try {
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("email", email);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(participateService.selectParticipateManage(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*참가신청관리 등록*/
    @PostMapping("/apply/{masterLicenseListObid}/{email}")
    public ResponseEntity<CommonResult> insertParticipateManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String masterLicenseListObid,
        @PathVariable String email,
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("email", email);
            participateService.insertParticipateManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*참가신청관리 변경*/
    @PatchMapping("/apply/{masterLicenseListObid}/{email}")
    public ResponseEntity<CommonResult> updateParticipateManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String masterLicenseListObid,
        @PathVariable String email,
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("email", email);
            participateService.updateParticipateManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*참가신청관리 승인/불가 다건*/
    @PatchMapping("/apply/list/{approval}")
    public ResponseEntity<CommonResult> updateParticipateManageListApproval(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String approval,
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            paramMap.put("approvalCode", approval);
            participateService.updateParticipateManageApprovalList(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*참가신청관리 승인/불가 단건*/
    @PatchMapping("/apply/{masterLicenseListObid}/{email}/{approval}")
    public ResponseEntity<CommonResult> updateParticipateManageApproval(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String masterLicenseListObid,
        @PathVariable String email,
        @PathVariable String approval,
        @RequestBody(required = false) Map<String, Object> paramMap) throws Exception
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("email", email);
            paramMap.put("approvalCode", approval);
            participateService.updateParticipateManageApproval(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*참가신청관리 삭제 단건*/
    @DeleteMapping("/apply/{masterLicenseListObid}/{email}")
    public ResponseEntity<CommonResult> deleteParticipateManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String masterLicenseListObid,
        @PathVariable String email,
        @RequestBody(required = false) Map<String, Object> paramMap) throws Exception
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("email", email);
            participateService.deleteParticipateManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }
}
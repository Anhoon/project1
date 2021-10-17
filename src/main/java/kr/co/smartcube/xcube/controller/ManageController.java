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

    /*참가신청관리*/
    /*참가신청관리 목록*/
    @GetMapping("/participate/list")
    public ResponseEntity<CommonResult> selectParticipateManageList(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody(required = false) Map<String, Object> paramMap)
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("email", loginVO.getEmail());
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(manageService.selectParticipateManageList(Util.initPaginagtion(paramMap))), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*참가신청관리 상세정보*/
    @GetMapping("/participate/{masterLicenseListObid}/{email}")
    public ResponseEntity<CommonResult> selectParticipateManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String masterLicenseListObid,
        @PathVariable String email,
        @RequestBody(required = false) Map<String, Object> paramMap) throws Exception 
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("email", email);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(manageService.selectParticipateManage(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*참가신청관리 등록*/
    @PostMapping("/participate/{masterLicenseListObid}/{email}")
    public ResponseEntity<CommonResult> insertParticipateManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String masterLicenseListObid,
        @PathVariable String email,
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("email", email);
            manageService.insertParticipateManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*참가신청관리 변경*/
    @PatchMapping("/participate/{masterLicenseListObid}/{email}")
    public ResponseEntity<CommonResult> updateParticipateManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String masterLicenseListObid,
        @PathVariable String email,
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("email", email);
            manageService.updateParticipateManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*참가신청관리 승인/불가 다건*/
    @PatchMapping("/participate/list/{approval}")
    public ResponseEntity<CommonResult> updateParticipateManageListApproval(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String approval,
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            paramMap.put("approvalCode", approval);
            manageService.updateParticipateManageApprovalList(paramMap);
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
    @PatchMapping("/participate/{masterLicenseListObid}/{email}/{approval}")
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
            manageService.updateParticipateManageApproval(paramMap);
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
    @DeleteMapping("/participate/{masterLicenseListObid}/{email}")
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
            manageService.deleteParticipateManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*권한관리*/
    /*권한관리 목록*/
    @GetMapping("/auth/list")
    public ResponseEntity<CommonResult> selectAuthManageList(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody(required = false) Map<String, Object> paramMap) throws Exception 
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("email", loginVO.getEmail());
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(manageService.selectAuthManageList(Util.initPaginagtion(paramMap))), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*권한관리 상세정보*/
    @GetMapping("/auth/{masterLicenseListObid}/{email}")
    public ResponseEntity<CommonResult> selectAuthManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String masterLicenseListObid,
        @PathVariable String email,
        @RequestBody(required = false) Map<String, Object> paramMap) throws Exception 
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("email", email);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(manageService.selectAuthManage(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*권한관리 등록*/
    @PostMapping("/auth/{masterLicenseListObid}/{email}")
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
            manageService.insertAuthManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*권한관리 수정*/
    @PatchMapping("/auth/{masterLicenseListObid}/{email}")
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
            manageService.updateAuthManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*권한관리 권한저장 다건*/
    @PatchMapping("/auth/list")
    public ResponseEntity<CommonResult> updateAuthManageList(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            manageService.updateAuthManageAuthList(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*권한관리 삭제 단건*/
    @DeleteMapping("/auth/{masterLicenseListObid}/{email}")
    public ResponseEntity<CommonResult> deleteAuthManage(
        @PathVariable String masterLicenseListObid,
        @PathVariable String email,
        @RequestBody(required = false) Map<String, Object> paramMap) throws Exception
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("email", email);
            manageService.deleteAuthManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*권한관리 삭제 다건*/
    @DeleteMapping("/auth/list")
    public ResponseEntity<CommonResult> deleteAuthManageList(
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            manageService.deleteAuthManageList(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*3D제작요청관리*/
    /*3D제작요청관리 목록*/
    @GetMapping("/3D/list")
    public ResponseEntity<CommonResult> select3DManageList(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody(required = false) Map<String, Object> paramMap) throws Exception 
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("email", loginVO.getEmail());
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(manageService.select3DManageList(Util.initPaginagtion(paramMap))), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*3D제작요청관리 상세정보*/
    @GetMapping("/3D/{obid}")
    public ResponseEntity<CommonResult> select3DManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String obid,
        @RequestBody(required = false) Map<String, Object> paramMap) throws Exception 
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("obid", obid);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(manageService.select3DManage(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*3D제작요청관리 등록*/
    @PostMapping("/3D/{masterLicenseListObid}/{email}")
    public ResponseEntity<CommonResult> insert3DManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String masterLicenseListObid,
        @PathVariable String email,
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("masterLicenseListObid", masterLicenseListObid);
            paramMap.put("email", email);
            manageService.insert3DManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*3D제작요청관리 수정*/
    @PatchMapping("/3D/{obid}")
    public ResponseEntity<CommonResult> update3DManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String obid,
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            paramMap.put("obid", obid);
            manageService.update3DManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*3D제작요청관리 삭제 단건*/
    @DeleteMapping("/3D/{obid}")
    public ResponseEntity<CommonResult> delete3DManage(
        @PathVariable String obid,
        @RequestBody(required = false) Map<String, Object> paramMap) throws Exception
    {
        try {
            if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();
            paramMap.put("obid", obid);
            manageService.delete3DManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*3D제작요청관리 삭제 다건*/
    @DeleteMapping("/3D/list")
    public ResponseEntity<CommonResult> delete3DManageList(
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            manageService.delete3DManageList(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }
}
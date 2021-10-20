package kr.co.smartcube.xcube.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import kr.co.smartcube.xcube.services.FileManageService;
import kr.co.smartcube.xcube.util.Util;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/file/manage")
public class FileManageController {

    @Autowired
    private FileManageService req3dService;
    
    @Autowired
    private ResponseService responseService;

    /*파일관리*/
    /*파일관리 목록*/
    @GetMapping("/list")
    public ResponseEntity<CommonResult> selectFileManageList(
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
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(req3dService.selectFileManageList(Util.initPaginagtion(paramMap))), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*파일관리 상세정보*/
    @GetMapping("/{obid}")
    public ResponseEntity<CommonResult> selectFileManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String obid) throws Exception 
    {
        try {
            Map<String, Object> paramMap = new HashMap<String,Object>();
            paramMap.put("obid", obid);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(req3dService.selectFileManage(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*파일관리 등록*/
    @PostMapping("")
    public ResponseEntity<CommonResult> insertFileManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            req3dService.insertFileManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*파일관리 수정*/
    @PatchMapping("/{obid}")
    public ResponseEntity<CommonResult> updateFileManage(
        @AuthenticationPrincipal LoginVO loginVO,
        @PathVariable String obid,
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            paramMap.put("obid", obid);
            req3dService.updateFileManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*파일관리 삭제 단건*/
    @DeleteMapping("/{obid}")
    public ResponseEntity<CommonResult> deleteFileManage(
        @PathVariable String obid) throws Exception
    {
        try {
            Map<String, Object> paramMap = new HashMap<String,Object>();
            paramMap.put("obid", obid);
            req3dService.deleteFileManage(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    /*파일관리 삭제 다건*/
    @DeleteMapping("/list")
    public ResponseEntity<CommonResult> deleteFileManageList(
        @RequestBody Map<String, Object> paramMap) throws Exception
    {
        try {
            req3dService.deleteFileManageList(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }
}
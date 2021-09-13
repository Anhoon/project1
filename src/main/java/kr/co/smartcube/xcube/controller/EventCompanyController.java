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
import kr.co.smartcube.xcube.services.EventCompanyService;
import kr.co.smartcube.xcube.util.Util;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/event/company")
public class EventCompanyController {

    @Autowired
    private EventCompanyService eventCompanyService;
    
    @Autowired
    private ResponseService responseService;

    @GetMapping("/license/master/")
    public ResponseEntity<CommonResult> selectMasterLicenseList(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pageNum", 0);
        map.put("pageSize", 2);
        map.put("orderBy", "1 desc");
        map = Util.initPaginagtion(map);
        try {
            return new ResponseEntity<CommonResult>(responseService.getListResult(eventCompanyService.selectMasterLicenseList(map)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/license/master/{obid}")
    public ResponseEntity<CommonResult> selectMasterLicense(@PathVariable String obid) throws Exception {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("obid", obid);
        try {
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(eventCompanyService.selectMasterLicense(paramMap)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/license/master/")
    public ResponseEntity<CommonResult> insertMasterLicense(@RequestBody Map<String, Object> map) throws Exception{
        try {
            eventCompanyService.insertMasterLicense(map);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
    }

    @PatchMapping("/license/master/")
    public ResponseEntity<CommonResult> updateMasterLicense(@RequestBody Map<String, Object> map){
        try {
            eventCompanyService.updateMasterLicense(map);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
    }

    @PatchMapping("/license/master/list")
    public ResponseEntity<CommonResult> updateMasterLicenseList(@RequestBody Map<String, Object> map){
        try {
            eventCompanyService.updateMasterLicenseList(map);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
    }
}
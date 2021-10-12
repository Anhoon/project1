package kr.co.smartcube.xcube.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import kr.co.smartcube.xcube.services.EventService;
import kr.co.smartcube.xcube.util.Util;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;
    
    @Autowired
    private ResponseService responseService;

    @GetMapping("")
    public ResponseEntity<CommonResult> selectEventList(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String searchKey,
            @RequestParam(required = false) String searchKeyWord
            ){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pageNum", 0);
        map.put("pageSize", 2);
        map.put("orderBy", "1 desc");
        if(!ObjectUtils.isEmpty(type)) map.put("type", type);
        if(!ObjectUtils.isEmpty(type)) map.put("searchKey", searchKey);
        if(!ObjectUtils.isEmpty(type)) map.put("searchKeyWord", searchKeyWord);
        map = Util.initPaginagtion(map);
        try {
            return new ResponseEntity<CommonResult>(responseService.getListResult(eventService.selectEventList(map)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{refObid}")
    public ResponseEntity<CommonResult> selectEvent(@PathVariable String refObid) throws Exception {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("refObid", refObid);
        try {
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(eventService.selectEvent(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("")
    public ResponseEntity<CommonResult> insertEventList(@RequestBody Map<String, Object> map) throws Exception{
        try {
            eventService.insertEventList(map);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
        
    }

    @PatchMapping("")
    public ResponseEntity<CommonResult> updateEventList(@RequestBody Map<String, Object> map){
        try {
            eventService.updateEventList(map);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<CommonResult> deleteEventList(@RequestBody Map<String, Object> map){
        try {
            eventService.deleteEventList(map);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }
}
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
import kr.co.smartcube.xcube.util.Util;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;
    
    @Autowired
    private ResponseService responseService;

    @GetMapping("/")
    public ResponseEntity<CommonResult> selectEventList(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pageNum", 0);
        map.put("pageSize", 2);
        map.put("orderBy", "1 desc");
        map = Util.initPaginagtion(map);
        try {
            return new ResponseEntity<CommonResult>(responseService.getListResult(eventService.selectEventList(map)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{refObid}")
    public ResponseEntity<CommonResult> selectEvent(@PathVariable String refObid) throws Exception {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("refObid", refObid);
        try {
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(eventService.selectEvent(paramMap)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/")
    public ResponseEntity<CommonResult> insertEventList(@RequestBody Map<String, Object> map) throws Exception{
        try {
            eventService.insertEventList(map);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
    }

    @PatchMapping("/")
    public ResponseEntity<CommonResult> updateMasterLicense(@RequestBody Map<String, Object> map){
        try {
            eventService.updateEvent(map);
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
    }
}
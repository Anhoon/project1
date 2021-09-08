package kr.co.smartcube.xcube.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smartcube.xcube.common.CommonResult;
import kr.co.smartcube.xcube.common.ResponseService;
import kr.co.smartcube.xcube.services.TestService;
import kr.co.smartcube.xcube.util.Util;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins="*")
@Slf4j
public class TestController {

  @Autowired
  private TestService testService;

  @Autowired
  private ResponseService responseService;
/*
  @GetMapping("/api/test")
  //public String test(@RequestBody Map<String, Object> payload) throws Exception {
    public String test() throws Exception {
    log.info("[TestController] API test");
    testService.test();
    return "!";
    
  }
*/

  @GetMapping("/api/test")
  public ResponseEntity<CommonResult> test(@RequestBody Map<String, Object> payload) throws Exception {
    payload = Util.initPaginagtion(payload);

    try {
      return new ResponseEntity<CommonResult>(
        responseService.getSingleResult(testService.selectHistory1(payload)), 
        HttpStatus.OK
      );
    } catch (Exception e) {
      return new ResponseEntity<CommonResult>(
        responseService.getFailResult(), 
        HttpStatus.CONFLICT
      );
    }
  } 
}
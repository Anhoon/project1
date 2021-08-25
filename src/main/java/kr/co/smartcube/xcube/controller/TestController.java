package kr.co.smartcube.xcube.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smartcube.xcube.services.TestService;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@Slf4j
public class TestController {

  @Autowired
  private TestService testService;

  @GetMapping("/api/test")
  //public String test(@RequestBody Map<String, Object> payload) throws Exception {
    public String test() throws Exception {
    log.info("[TestController] API test");
    testService.test();
    return "!";
    
  }
  
}
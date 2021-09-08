package kr.co.smartcube.xcube.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smartcube.xcube.common.CommonResult;
import kr.co.smartcube.xcube.common.ResponseService;
import kr.co.smartcube.xcube.services.LoginService;
import kr.co.smartcube.xcube.util.Util;

@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    private ResponseService responseService;

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/api/login")
	public ResponseEntity<CommonResult> login(@RequestBody Map<String, Object> paramMap) throws Exception {
        if(Util.isEmpty(paramMap) || Util.isEmpty(paramMap.get("email")) || Util.isEmpty(paramMap.get("password"))){
            return new ResponseEntity<CommonResult>(responseService.getLoginFailResult(), HttpStatus.CONFLICT);
        }

        try {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            Map<String, Object> token = loginService.login(paramMap);
		    headers.add("at-jwt-access-token", (String) token.get("accessToken"));
            headers.add("at-jwt-refresh-token", (String) token.get("refreshToken"));
            
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(token), headers, HttpStatus.OK);  
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getLoginFailResult(), HttpStatus.CONFLICT);
        }
	}

    @PostMapping(value = "/api/login/refresh")
	public ResponseEntity<CommonResult> refreshToken(@RequestBody Map<String, Object> paramMap) throws Exception {
        if(Util.isEmpty(paramMap) || Util.isEmpty(paramMap.get("accessToken")) || Util.isEmpty(paramMap.get("refreshToken"))){
            return new ResponseEntity<CommonResult>(responseService.getFailResult("accessToken 또는 refreshToken가 존재하지 않습니다."), HttpStatus.CONFLICT);  
        }

        try {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            Map<String, Object> token = loginService.refreshToken(paramMap);
		    headers.add("at-jwt-access-token", (String) token.get("accessToken"));
            headers.add("at-jwt-refresh-token", (String) token.get("refreshToken"));

            return new ResponseEntity<CommonResult>(responseService.getSingleResult(token), headers, HttpStatus.OK);  
        } catch (RuntimeException e){
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);  
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);  
        }
	}

    /*
    @PostMapping(value = "/api/logout")
	public ResponseEntity<CommonResult> logout() throws Exception {
        Map<String, Object> paramMap = new HashMap<String,Object>();
        paramMap.put("email", (String) SecurityUtil.getCurrentUserId());

        try {
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(loginService.logout(paramMap)), HttpStatus.NO_CONTENT);  
        } catch (Exception e) {
            return new ResponseEntity<CommonResult>(responseService.getLoginFailResult(), HttpStatus.CONFLICT);
        }
	}
    */
}
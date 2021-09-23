package kr.co.smartcube.xcube.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smartcube.xcube.common.CommonResult;
import kr.co.smartcube.xcube.common.ResponseService;
import kr.co.smartcube.xcube.common.security.jwt.TokenProvider;
import kr.co.smartcube.xcube.services.LoginService;
import kr.co.smartcube.xcube.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins="*")
@Slf4j
public class LoginController {

    @Autowired
    private ResponseService responseService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping(value = "/api/login")
	public ResponseEntity<CommonResult> login(@RequestBody Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
        if(ObjectUtils.isEmpty(paramMap) || ObjectUtils.isEmpty(paramMap.get("email")) || ObjectUtils.isEmpty(paramMap.get("password"))){
            return new ResponseEntity<CommonResult>(responseService.getLoginFailResult(), HttpStatus.CONFLICT);
        }

        try {
            Map<String, Object> returnMap = loginService.login(paramMap, response);
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(returnMap), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getLoginFailResult(), HttpStatus.CONFLICT);
        }
	}

    @PostMapping(value = "/api/login/refresh")
	public ResponseEntity<CommonResult> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {
            String accessToken = tokenProvider.getAccessToken(request);
            String refreshToken = tokenProvider.getRefreshToken(request);
            
            if(!StringUtils.hasLength(accessToken) || !StringUtils.hasLength(refreshToken)){
                return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);  
            }

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("accessToken", accessToken);
            paramMap.put("refreshToken", refreshToken);
            
            Map<String, Object> returnMap = loginService.refreshToken(paramMap, response);
            returnMap.remove("user");

            return new ResponseEntity<CommonResult>(responseService.getSingleResult(returnMap), HttpStatus.OK);  
        } catch (RuntimeException e){
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);  
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);  
        }
	}

    @PostMapping(value = "/api/logout")
	public ResponseEntity<CommonResult> logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String accessToken = tokenProvider.getAccessToken(request);
            String refreshToken = tokenProvider.getRefreshToken(request);
            
            if(!StringUtils.hasLength(accessToken) || !StringUtils.hasLength(refreshToken)){
                return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);  
            }

            SecurityUtil.setLoginHeader(new HashMap<String,Object>(), response);

            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);  
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
	}
}
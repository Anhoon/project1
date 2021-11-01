package kr.co.springboot.project.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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

import kr.co.springboot.project.common.CommonResult;
import kr.co.springboot.project.common.ResponseService;
import kr.co.springboot.project.services.UserService;
import kr.co.springboot.project.util.Util;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseService responseService;

    @GetMapping("")
    public ResponseEntity<CommonResult> selectUserList(
            @RequestParam(required = false) String searchKey,
            @RequestParam(required = false) String searchKeyWord,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderBy) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pageNum", 0);
        map.put("pageSize", 2);
        map.put("orderBy", "1 desc");
        map = Util.initPaginagtion(map);
        if(!ObjectUtils.isEmpty(searchKey)) map.put("searchKey", searchKey);
        if(!ObjectUtils.isEmpty(searchKeyWord)) map.put("searchKeyWord", searchKeyWord);
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        map.put("orderBy", orderBy);
        try {
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(userService.selectUserList(Util.initPaginagtion(map))), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<CommonResult> selectUser(@PathVariable String email) throws Exception {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("email", email);
        try {
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(userService.selectUser(paramMap)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("")
    public ResponseEntity<CommonResult> insertData(@RequestBody Map<String, Object> map) throws Exception{
        try {
            userService.insertUser(map);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("")
    public ResponseEntity<CommonResult> updateData(@RequestBody Map<String, Object> map) throws Exception{
        try {
            userService.updateUser(map);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<CommonResult> deleteData(@RequestBody Map<String, Object> map) throws Exception{
        try {
            userService.deleteUserStatus(map);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/certified")
    public ResponseEntity<CommonResult> mailCheck(@RequestParam(value = "obid") String obid, @RequestParam(value = "email") String email
    ,HttpServletResponse respon) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("obid", obid);
        map.put("email", email);
        try {
            userService.userCertified(map);
            String newURL = respon.encodeRedirectURL("http://localhost:3000/");
            respon.sendRedirect(newURL);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(),HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
        
    }

    @GetMapping("/findUser/{name}")
    public ResponseEntity<CommonResult> findUser(@PathVariable String name) throws Exception {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("userName", name);
        try {
            userService.userCheck(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/findPassword/{email}")
    public ResponseEntity<CommonResult> findPassword(@PathVariable String email) throws Exception{
        try {
            userService.findPassword(email);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/userCheck/{email}")
    public ResponseEntity<CommonResult> userCheck(@PathVariable String email) throws Exception {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("email", email);
        try {
            userService.userCheck(paramMap);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }
}
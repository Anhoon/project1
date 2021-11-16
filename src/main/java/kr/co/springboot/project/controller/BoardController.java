package kr.co.springboot.project.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.springboot.project.common.CommonResult;
import kr.co.springboot.project.common.ResponseService;
import kr.co.springboot.project.services.BoardService;
import kr.co.springboot.project.util.Util;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins="*")
@Slf4j
public class BoardController {

  @Autowired
  private BoardService boardService;

  @Autowired
  private ResponseService responseService;

  @GetMapping("/api/board/list")
  public ResponseEntity<CommonResult> selectBoardList(
        @RequestParam(required = false) String searchKey,
        @RequestParam(required = false) String searchKeyWord,
        @RequestParam(required = false, defaultValue = "1") Integer pageNum,
        @RequestParam(required = false, defaultValue = "5") Integer pageSize,
        @RequestParam(required = false) String orderBy) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map = Util.initPaginagtion(map);
        map.put("searchKey", searchKey);
        map.put("searchKeyWord", searchKeyWord);
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        map.put("orderBy", orderBy);
        System.out.println(map);
        
        try {
            return new ResponseEntity<CommonResult>(responseService.getSingleResult(boardService.selectBoardList(Util.initPaginagtion(map))), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/api/board/insert")
    public ResponseEntity<CommonResult> insertBoard(@RequestBody Map<String, Object> map) throws Exception{
        try {
            boardService.insertBoard(map);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/api/board/update")
    public ResponseEntity<CommonResult> updateBoard(@RequestBody Map<String, Object> map) throws Exception{
        try {
            boardService.updateBoard(map);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/api/board/delete")
    public ResponseEntity<CommonResult> deleteBoard(@RequestBody Map<String, Object> map) throws Exception{
        try {
            boardService.deleteBoard(map);
            return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
        }
    }    

}



package kr.co.smartcube.xcube.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smartcube.xcube.common.CommonResult;
import kr.co.smartcube.xcube.common.ResponseService;
import kr.co.smartcube.xcube.common.security.jwt.LoginVO;
import kr.co.smartcube.xcube.services.BoardService;
import kr.co.smartcube.xcube.util.Util;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins="*")
@Slf4j
public class BoardController {

  @Autowired
  private ResponseService responseService;

  @Autowired
  private BoardService boardService;

  /*일반 게시판용*/
  /*자료등록*/
  @GetMapping("api/event/company/file/{boardType}")
  public ResponseEntity<CommonResult> selectFileBoardList(
      @AuthenticationPrincipal LoginVO loginVO,
      @PathVariable String boardType, 
      @RequestBody(required = false) Map<String, Object> paramMap) throws Exception
  {
    if(!boardType.equals("image") && !boardType.equals("normal") && !boardType.equals("3D")) 
      return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.NOT_FOUND);
    
    if(ObjectUtils.isEmpty(paramMap)) 
      paramMap = new HashMap<String,Object>();

    paramMap.put("boardType", boardType);
    
    try {
      return new ResponseEntity<CommonResult>(responseService.getSuccessResult(boardService.selectBoardList(Util.initPaginagtion(paramMap))), HttpStatus.OK);
    } catch (RuntimeException e) {
      return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
    } catch (Exception e) {
      return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
    }
  }

  @GetMapping("api/event/company/file/{boardType}/{obid}")
  public ResponseEntity<CommonResult> selectFileBoard(
      @AuthenticationPrincipal LoginVO loginVO,
      @PathVariable String boardType, 
      @PathVariable String obid, 
      @RequestBody(required = false) Map<String, Object> paramMap) throws Exception
  {
    if(!boardType.equals("image") && !boardType.equals("normal") && !boardType.equals("3D")) 
      return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.NOT_FOUND);
    
    if(ObjectUtils.isEmpty(paramMap)) 
      paramMap = new HashMap<String,Object>();

    paramMap.put("boardType", boardType);
    paramMap.put("obid", obid);
    
    try {
      return new ResponseEntity<CommonResult>(responseService.getSuccessResult(boardService.selectBoard(paramMap)), HttpStatus.OK);
    } catch (RuntimeException e) {
      return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
    } catch (Exception e) {
      return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
    }
  }

  @PostMapping("api/event/company/file/{boardType}")
  public ResponseEntity<CommonResult> insertFileBoardInfo(
    @AuthenticationPrincipal LoginVO loginVO,
    @PathVariable String boardType, 
    @RequestBody Map<String, Object> paramMap) throws Exception
  {
    if(!boardType.equals("image") && !boardType.equals("normal") && !boardType.equals("3D")) 
      return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.NOT_FOUND);

    paramMap.put("fileGroup", boardType);
    paramMap.put("boardType", boardType);
    paramMap.put("regUsrId", loginVO.getEmail());
    paramMap.put("regUsrNm", loginVO.getName());
    
    try {
      boardService.insertBoard(paramMap); 
      return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
    } catch (RuntimeException e) {
      return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
    } catch (Exception e) {
      return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
    }
  }

  @PatchMapping("api/event/company/file/{boardType}/{obid}")
  public ResponseEntity<CommonResult> updateFileBoard(
    @AuthenticationPrincipal LoginVO loginVO,
    @PathVariable String boardType, 
    @PathVariable String obid, 
    @RequestBody Map<String, Object> paramMap) throws Exception
  {
    if(!boardType.equals("image") && !boardType.equals("normal") && !boardType.equals("3D")) 
      return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.NOT_FOUND);
    
    paramMap.put("fileGroup", boardType);
    paramMap.put("boardType", boardType);
    paramMap.put("obid", obid);
    paramMap.put("updUsrId", loginVO.getEmail());
    paramMap.put("updUsrNm", loginVO.getName());
    
    try {
      boardService.updateBoard(paramMap); 
      return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
    } catch (RuntimeException e) {
      return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
    } catch (Exception e) {
      return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
    }
  }

  @DeleteMapping("api/event/company/file/{boardType}/{obid}")
  public ResponseEntity<CommonResult> deleteFileBoard(
    @AuthenticationPrincipal LoginVO loginVO,
    @PathVariable String boardType, 
    @PathVariable String obid, 
    @RequestBody(required = false) Map<String, Object> paramMap) throws Exception
  {
    if(!boardType.equals("image") && !boardType.equals("normal") && !boardType.equals("3D")) 
      return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.NOT_FOUND);
    
    if(ObjectUtils.isEmpty(paramMap)) paramMap = new HashMap<String,Object>();

    paramMap.put("fileGroup", boardType);
    paramMap.put("boardType", boardType);
    paramMap.put("obid", obid);
    paramMap.put("updUsrId", loginVO.getEmail());
    paramMap.put("updUsrNm", loginVO.getName());
    
    try {
      boardService.deleteBoard(paramMap); 
      return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
    } catch (RuntimeException e) {
      return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
    } catch (Exception e) {
      return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
    }
  }

  @DeleteMapping("api/event/company/file/{boardType}")
  public ResponseEntity<CommonResult> deleteFileBoardList(
    @AuthenticationPrincipal LoginVO loginVO,
    @PathVariable String boardType, 
    @RequestBody Map<String, Object> paramMap) throws Exception
  {
    if(!boardType.equals("image") && !boardType.equals("normal") && !boardType.equals("3D")) 
      return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.NOT_FOUND);
    
    if(ObjectUtils.isEmpty(paramMap) || ObjectUtils.isEmpty(paramMap.get("obid"))) 
      return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
    
    try {
      boardService.deleteBoardList(paramMap); 
      return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.OK);
    } catch (RuntimeException e) {
      e.printStackTrace();
      return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
    }
  }
}
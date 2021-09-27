package kr.co.smartcube.xcube.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.core.joran.conditional.ElseAction;
import kr.co.smartcube.xcube.common.CommonResult;
import kr.co.smartcube.xcube.common.ResponseService;
import kr.co.smartcube.xcube.services.FileService;
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

  @Autowired
  private FileService fileService;

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
    try {
      return new ResponseEntity<CommonResult>(
        responseService.getSingleResult(testService.selectHistory1(Util.initPaginagtion(payload))), 
        HttpStatus.OK
      );
    } catch (Exception e) {
      return new ResponseEntity<CommonResult>(
        responseService.getFailResult(), 
        HttpStatus.CONFLICT
      );
    }
  } 
 
  @PostMapping("/api/test/upload")
  public ResponseEntity<CommonResult> uploadMultipleFiles(
      @RequestParam("files") MultipartFile[] files, 
      @RequestParam(required = false) Map<String,Object> paramMap) throws Exception
  {
      List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
      
      try {
        fileList = fileService.storeFile(files, paramMap);
      } catch (FileUploadException e) {
        return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
      } catch (Exception e) {
        return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
      }

      return new ResponseEntity<CommonResult>(responseService.getListResult(fileList), HttpStatus.CREATED);
  }
  
  @GetMapping(value = {"/api/test/download/{fileName:.+}", 
              "/api/test/download/{fileGroup}/{fileName:.+}", 
              "/api/test/download/{fileGroup}/{fileSubGroup}/{fileName:.+}"})
  public ResponseEntity<Object> downloadFile(
    @PathVariable(required = true) String fileName,   
    @PathVariable(required = false) String fileGroup, 
    @PathVariable(required = false) String fileSubGroup, 
    @RequestHeader("User-Agent") String agent,
    HttpServletRequest request) throws Exception{
      Map<String, Object> paramMap = new HashMap<String, Object>();
      String contentType = null;
      Resource resource = null;
      
      paramMap.put("fileGroup", fileGroup);
      paramMap.put("fileSubGroup", fileSubGroup);
      paramMap.put("fileName", fileName);

      try {
        // Load file as Resource
        resource = fileService.loadFileAsResource(paramMap);

        // Try to determine file's content type
        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        // Fallback to the default content type if type could not be determined
        if(!StringUtils.hasText(contentType)) contentType = "application/octet-stream";
      
        String savedFileName = URLDecoder.decode(resource.getFilename(), "UTF-8");
        String originFileName = savedFileName.substring(savedFileName.lastIndexOf("_")+1);

        if(agent.contains("Trident")) //Internet Explorer
          originFileName = URLDecoder.decode(originFileName, "UTF-8").replaceAll("\\+", " ");
        else if(agent.contains("Edge")) //Micro Edge
          originFileName = URLDecoder.decode(originFileName, "UTF-8");
        else 
          originFileName = new String(originFileName.getBytes("UTF-8"), "ISO-8859-1");
        
        return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originFileName  + "\"")
          .body(resource);
      } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseService.getFailResult("Could not determine file type."));
      } catch (RuntimeException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseService.getFailResult(e.getMessage()));
      }catch (Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseService.getFailResult());
      }
  }

  @PostMapping("/api/test/{board}")
  public ResponseEntity<CommonResult> insertBoardInfo(
      @PathVariable String board, @RequestBody Map<String, Object> paramMap) throws Exception{
        paramMap.put("fileGroup", board);
        
        try {
          testService.insertBoardInfo(paramMap);  
        } catch (Exception e) {
          return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
        }
        
    return new ResponseEntity<CommonResult>(responseService.getSuccessResult(), HttpStatus.CREATED);
  }

}
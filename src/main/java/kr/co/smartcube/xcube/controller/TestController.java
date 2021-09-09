package kr.co.smartcube.xcube.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import kr.co.smartcube.xcube.common.CommonResult;
import kr.co.smartcube.xcube.common.ResponseService;
import kr.co.smartcube.xcube.services.FileUploadDownloadService;
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
  private FileUploadDownloadService fileService;

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
 
  @PostMapping("/api/test/upload")
  public ResponseEntity<CommonResult> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("email") String email) throws Exception{
      List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
      try {
        fileList = fileService.storeFile(files);
      } catch (FileUploadException e) {
        return new ResponseEntity<CommonResult>(responseService.getFailResult(e.getMessage()), HttpStatus.CONFLICT);
      } catch (Exception e) {
        return new ResponseEntity<CommonResult>(responseService.getFailResult(), HttpStatus.CONFLICT);
      }

      return new ResponseEntity<CommonResult>(responseService.getListResult(fileList), HttpStatus.CREATED);
  }
  
  @GetMapping("/api/test/download/{fileName:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws Exception{
       // Load file as Resource
      Resource resource = fileService.loadFileAsResource(fileName);

      // Try to determine file's content type
      String contentType = null;
      try {
          contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
      } catch (IOException ex) {
          log.info("Could not determine file type.");
      }

      // Fallback to the default content type if type could not be determined
      if(contentType == null) {
          contentType = "application/octet-stream";
      }

      return ResponseEntity.ok()
              .contentType(MediaType.parseMediaType(contentType))
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
              .body(resource);
  }

}
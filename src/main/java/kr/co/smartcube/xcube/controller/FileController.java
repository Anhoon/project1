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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.co.smartcube.xcube.common.CommonResult;
import kr.co.smartcube.xcube.common.ResponseService;
import kr.co.smartcube.xcube.services.FileUploadDownloadService;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins="*")
@Slf4j
public class FileController {

  @Autowired
  private ResponseService responseService;

  @Autowired
  private FileUploadDownloadService fileService;
 
  @PostMapping("/api/file/upload")
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
  
  @GetMapping(value = {"/api/file/download/{fileName:.+}", 
              "/api/file/download/{fileGroup}/{fileName:.+}", 
              "/api/file/download/{fileGroup}/{fileSubGroup}/{fileName:.+}"})
  public ResponseEntity<Object> downloadFile( 
    @PathVariable String fileName, 
    @PathVariable(required = false) String fileGroup, 
    @PathVariable(required = false) String fileSubGroup, 
    HttpServletRequest request) throws Exception
  {
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
      if(contentType == null) {
        contentType = "application/octet-stream";
      }
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(responseService.getFailResult("Could not determine file type."));
    } catch (RuntimeException e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseService.getFailResult(e.getMessage()));
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseService.getFailResult());
    }

    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLDecoder.decode(resource.getFilename(), "UTF-8")  + "\"")
            .body(resource);
  }

}
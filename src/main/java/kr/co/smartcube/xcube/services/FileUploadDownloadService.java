package kr.co.smartcube.xcube.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.co.smartcube.xcube.common.FileUploadProperties;
import kr.co.smartcube.xcube.util.Util;

@Service
public class FileUploadDownloadService {
    private final Path fileLocation;
    
    @Autowired
    public FileUploadDownloadService(FileUploadProperties prop) throws Exception{
        this.fileLocation = Paths.get(prop.getUploadDir()).toAbsolutePath().normalize();
        System.out.println(fileLocation.toString());
        try {
            Files.createDirectories(this.fileLocation);
        }catch(Exception e) {
            throw new FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
        }
    }

    public List<Map<String, Object>> storeFile(MultipartFile[] files) throws Exception {
        List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
        Map<String, Object> paramMap = null;

        try {
            for(MultipartFile file : files){
                paramMap = new HashMap<String,Object>();

                storeFile(file);
                paramMap = Util.fileToFileInfoMap(file);
                fileList.add(paramMap);
            }
        } catch (FileUploadException e) {
            
        } catch (Exception e) {
            
        }
        return fileList;
    }

    public Map<String,Object> storeFile(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        try {
            // 파일명에 부적합 문자가 있는지 확인한다.
            if(fileName.contains("..")){
                throw new FileUploadException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);
            }
            Path targetLocation = this.fileLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return Util.fileToFileInfoMap(file);
        }catch(Exception e) {
            throw new FileUploadException("["+fileName+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",e);
        }
    }

    public Resource loadFileAsResource(String fileName) throws Exception {
        try {
            Path filePath = this.fileLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if(resource.exists()) {
                return resource;
            }else {
                throw new RuntimeException(fileName + " 파일을 찾을 수 없습니다.");
            }
        } catch(MalformedURLException e) {
            throw new RuntimeException(fileName + " 파일을 찾을 수 없습니다.", e);
        }
    }

    public Map<String, Object> fileUploadByte(String fileName, String fileBase64) throws Exception{

        try {
            String regex = "^\\S+;(base64,)";
            fileBase64 = fileBase64.replaceAll(regex, "");
            if(Util.isEmpty(fileBase64)) {

                throw new RuntimeException(fileName + " 파일을 찾을 수 없습니다.");
            }
            // 파일명에 부적합 문자가 있는지 확인한다.
            if(fileName.contains("..")){
                throw new FileUploadException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);
            }
            Path targetLocation = this.fileLocation.resolve(fileName);
            File file = new File(targetLocation.toString());
            // BASE64를 일반 파일로 변환하고 저장합니다.
            
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decodedBytes = decoder.decode(fileBase64.getBytes());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(decodedBytes);
            fileOutputStream.close();
            return Util.fileToFileInfoMap(file);
        } catch(IOException e) {
            throw new IOException("["+fileName+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",e);
        } catch(Exception e) {
            throw new FileUploadException("["+fileName+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",e);
        }
    }
}

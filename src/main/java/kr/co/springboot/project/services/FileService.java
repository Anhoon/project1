package kr.co.springboot.project.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.co.springboot.project.common.FileUploadProperties;
import kr.co.springboot.project.mybatis.dao.FileDao;
import kr.co.springboot.project.util.Util;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileService {
    private final Path fileLocation;

    @Autowired
    private FileDao fileDao;
    
    @Autowired
    public FileService(FileUploadProperties prop) throws Exception{
        this.fileLocation = Paths.get(prop.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileLocation);
        }catch(Exception e) {
            throw new FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
        }
    }

    public Map<String,Object> storeFile(MultipartFile file) throws Exception {
        return storeFile(file, null);
    }

    public List<Map<String, Object>> storeFile(MultipartFile[] files) throws Exception {
       return storeFile(files, null);
    }

    public List<Map<String, Object>> storeFile(MultipartFile[] files, Map<String, Object> fileInfo) throws Exception {
        List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
        try {
            for(MultipartFile file : files){
                storeFile(file, fileInfo);
                fileList.add(Util.fileToFileInfoMap(file, fileInfo));
            }
        } catch (FileUploadException e) {
            throw new FileUploadException("파일 업로드에 실패하였습니다. 다시 시도하십시오.",e);
        } catch (Exception e) {
            throw new Exception("파일 업로드에 실패하였습니다. 다시 시도하십시오.", e);
        }
        return fileList;
    }

    public Map<String,Object> storeFile(MultipartFile file, Map<String, Object> fileInfo) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path path = uploadPath(fileInfo);

        try {
            // 파일명에 부적합 문자가 있는지 확인한다.
            if(fileName.contains("..")){
                throw new FileUploadException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);
            }

            Path targetLocation = path.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return Util.fileToFileInfoMap(file, fileInfo);
        }catch(Exception e) {
            throw new FileUploadException("["+fileName+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",e);
        }
    }

    public Resource loadFileAsResource(Map<String,Object> paramMap) throws Exception {
        Path path = uploadPath(paramMap);
        String fileName =  (String) paramMap.get("fileName");
        
        try {
            Path filePath = path.resolve(fileName).normalize();
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
        return fileUploadByte(fileName, fileBase64, null, null);
    }

    public Map<String, Object> fileUploadByte(String fileName, String fileBase64, Map<String, Object> fileGroupInfoMap) throws Exception{
        return fileUploadByte(fileName, fileBase64, null, fileGroupInfoMap);
    }

    public Map<String, Object> fileUploadByte(String fileName, String fileBase64, String refObid, Map<String, Object> fileGroupInfoMap) throws Exception{
        try {
            Path path = uploadPath(fileGroupInfoMap);
            String regex = "^\\S+;(base64,)";

            fileBase64 = fileBase64.replaceAll(regex, "");

            if(!StringUtils.hasLength(fileBase64)) {
                throw new RuntimeException(fileName + " 파일을 찾을 수 없습니다.");
            }

            // 파일명에 부적합 문자가 있는지 확인한다.
            if(fileName.contains("..")){
                throw new FileUploadException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);
            }

            String now = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());  //현재시간
            
            Path targetLocation = path.resolve(now+"_"+fileName);
            File file = new File(targetLocation.toString());
            
            // BASE64를 일반 파일로 변환하고 저장합니다.
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decodedBytes = decoder.decode(fileBase64.getBytes());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(decodedBytes);
            fileOutputStream.close();
            return Util.fileToFileInfoMap(file, refObid, fileGroupInfoMap);
        } catch(IOException e) {
            throw new IOException("["+fileName+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",e);
        } catch(Exception e) {
            throw new FileUploadException("["+fileName+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",e);
        }
    }

    private Path uploadPath(Map<String, Object> fileGroupInfoMap) throws Exception{
        Path path = this.fileLocation;

        if(!ObjectUtils.isEmpty(fileGroupInfoMap) && !ObjectUtils.isEmpty(fileGroupInfoMap.get("fileGroup"))){
            path = Paths.get(path.toString()+"/"+fileGroupInfoMap.get("fileGroup")).toAbsolutePath().normalize();
            
            if(!ObjectUtils.isEmpty(fileGroupInfoMap.get("fileSubGroup"))){
                path = Paths.get(path.toString()+"/"+fileGroupInfoMap.get("fileSubGroup")).toAbsolutePath().normalize();
            }

            Files.createDirectories(path);
        }

        return path;
    }

    private int deleteRealFile(Map<String,Object> paramMap) throws Exception{
        int delFileCnt = 0;
        
        if(!ObjectUtils.isEmpty(paramMap.get("filePath")) && !ObjectUtils.isEmpty(paramMap.get("fileName"))){
            String filePath = paramMap.get("filePath").toString()
                                            .replaceAll("http://localhost:8080/api/file/download/", "")
                                            .replaceAll("http://localhost:8080/api/test/download/", "");

            File file = new File(this.fileLocation.toString()+"\\"+filePath);

            if(file.exists()){
                if(file.delete()){
                    log.info("파일삭제 성공");
                    delFileCnt++;
                }else{ 
                    log.info("파일삭제 실패");
                }
            }else{ 
                log.info("파일이 존재하지 않습니다.");
            }
        }
        return delFileCnt;

    }

    public int deleteFileList(Map<String,Object> paramMap) throws Exception{
        if(ObjectUtils.isEmpty(paramMap) || ObjectUtils.isEmpty(paramMap.get("refObid"))){
            return 0;
        }
        
        List<Map<String, Object>> fileList = fileDao.selectFileList(paramMap);
        for(Map<String, Object> file : fileList){
            deleteRealFile(file);
        }
        return fileDao.deleteFileList(paramMap);
    }

    public int deleteFile(Map<String,Object> paramMap) throws Exception{
        if(ObjectUtils.isEmpty(paramMap) || ObjectUtils.isEmpty(paramMap.get("obid"))){
            return 0;
        }

        deleteRealFile(paramMap);
        return fileDao.deleteFile(paramMap);
    }

    public List<Map<String, Object>> selectFileList(Map<String, Object> paramMap) throws Exception {
        return fileDao.selectFileList(paramMap);
    }

	public Map<String, Object> selectFile(Map<String, Object> paramMap)  throws Exception{
		return fileDao.selectFile(paramMap);
    }

	public int insertFile(List<Map<String, Object>> paramList)  throws Exception{
		return fileDao.insertFile(paramList);
	}

	public int insertFileList(List<Map<String, Object>> paramList)  throws Exception{
		return fileDao.insertFileList(paramList);
	}
}

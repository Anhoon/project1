package kr.co.smartcube.xcube.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties.Format;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class Util {
	public static boolean isEmpty(Object obj) {
		if(obj == null) return true;
		if ((obj instanceof String) && (((String)obj).trim().length() == 0)) { return true; }

	        if (obj instanceof Map) { return ((Map<?, ?>) obj).isEmpty(); }

	        if (obj instanceof Map) { return ((Map<?, ?>)obj).isEmpty(); } 

	        if (obj instanceof List) { return ((List<?>)obj).isEmpty(); }

	        if (obj instanceof Object[]) { return (((Object[])obj).length == 0); }
		return false;

	}
    public static Map<String,Object> getResult(boolean flag){
        Map<String,Object> returnMap = new HashMap<String,Object>();
        if(flag){
            returnMap.put("status", HttpStatus.OK);
            returnMap.put("message","성공");
        }else{
            returnMap.put("status", HttpStatus.NOT_FOUND);
            returnMap.put("message","실패");
        }
        return returnMap;
    }

    public static String objToJson(Object obj){
        ObjectMapper objectMapper =new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.info(e.toString());
        }
        return jsonStr;
    }

    public static Map<String,Object> jsonToMap(String json){
        ObjectMapper objectMapper =new ObjectMapper();
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        } catch (JsonGenerationException e) { 
            e.printStackTrace(); 
        } 
        catch (JsonMappingException e) { 
            e.printStackTrace(); 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
        return map;
    }

    public static ArrayList<Object> jsonToArray(String json){
        ObjectMapper objectMapper =new ObjectMapper();
        ArrayList<Object> array = new ArrayList<Object>();
        try {
            array = objectMapper.readValue(json, new TypeReference<ArrayList<Object>>(){});
        } catch (JsonGenerationException e) { 
            e.printStackTrace(); 
        } 
        catch (JsonMappingException e) { 
            e.printStackTrace(); 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
        return array;
    }

    public static String objToStr(Object param){
        String returnParam = "";
        
        if(isEmpty(param)) return "";

        if(param instanceof Integer){
            returnParam = Integer.toString((Integer)param);
        } else if(param instanceof String){
            returnParam = (String) param;
        }
        return returnParam;
    }

    public static Map<String, Object> initPaginagtion(Map<String, Object> param){
        if(param.get("pageNum") == null){
            param.put("pageNum", 0);
        }

        if(param.get("pageSize") == null){
            param.put("pageSize", 2);
        }
        
        if(param.get("orderBy") == null){
            param.put("orderBy", "1 desc");
        }

        return param;
    }

    public static Map<String,Object> fileToFileInfoMap(MultipartFile file){
        Map<String,Object> map = new HashMap<String, Object>();

        if(!isEmpty(file)){
            map.put("obid", UUID.randomUUID().toString());
            map.put("fileName", StringUtils.cleanPath(file.getOriginalFilename()));
            map.put("fileContentType", file.getContentType());
            map.put("fileSize", file.getSize());
            map.put("fileDownLoadUrl", ServletUriComponentsBuilder.fromCurrentContextPath().toUriString()+"/api/test/download/"+file.getOriginalFilename());
        }
        
        return map;
    }
    
    public static Map<String,Object> fileToFileInfoMap(File file){
        Map<String,Object> map = new HashMap<String, Object>();

        if(!isEmpty(file)){
            map.put("obid", UUID.randomUUID().toString());
            map.put("fileName", StringUtils.cleanPath(file.getName()));
            map.put("fileContentType", file.getPath());
            map.put("fileSize", file.length());
            map.put("fileDownLoadUrl", ServletUriComponentsBuilder.fromCurrentContextPath().toUriString()+"/api/test/download/"+file.getName());
        }
        
        return map;
    }

    public static List<Map<String,Object>> objToList(Object param){
        List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
        if(isEmpty(param)) return returnList;
        if(param instanceof List){
            returnList = (List<Map<String,Object>>) param;
        }else if(param instanceof ArrayList){
            returnList = (List<Map<String,Object>>) param;
        }
        return returnList;
    }

    public static Map<String,Object> objToMap(Object param){
        Map<String,Object> returnMap = new HashMap<String,Object>();
        
        if(isEmpty(param)) return returnMap;

        if(param instanceof Map){
            returnMap = (Map<String,Object>) param;
        }
        return returnMap;
    }

    public static boolean dateCheck(Object date) {
        String strDate = Util.objToStr(date);
        boolean flag = false;
        flag = Util.validationDate(strDate, "yyyy-MM-dd HH:mm:ss");
        if(!flag){
            flag = Util.validationDate(strDate, "yyyyMMddHHmmss");
        }
        return flag;
    }

    public static boolean validationDate(String date, String format){
        SimpleDateFormat dateFormatParser = new SimpleDateFormat(format);
        dateFormatParser.setLenient(false);
        try {
            dateFormatParser.parse(date);
            return true;
        } catch (Exception Ex) {
            return false;
        }
    }
}
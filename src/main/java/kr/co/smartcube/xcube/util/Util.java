package kr.co.smartcube.xcube.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;

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
}
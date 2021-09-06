package kr.co.smartcube.xcube.util;

import java.util.HashMap;
import org.springframework.jdbc.support.JdbcUtils;

@SuppressWarnings("serial")
public class ParamMap extends HashMap<String, Object> {

    @Override
    public Object put(String key, Object value) {
        return super.put(JdbcUtils.convertUnderscoreNameToPropertyName(key), value);
    }
    
}
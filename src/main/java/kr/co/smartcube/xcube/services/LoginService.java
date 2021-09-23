package kr.co.smartcube.xcube.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import kr.co.smartcube.xcube.common.security.jwt.LoginVO;
import kr.co.smartcube.xcube.common.security.jwt.TokenProvider;
import kr.co.smartcube.xcube.mybatis.dao.LoginDao;
import kr.co.smartcube.xcube.util.SecurityUtil;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

	@Autowired
    private LoginDao loginDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Map<String, Object> paramMap = new HashMap<String,Object>();
        paramMap.put("email", username);

		LoginVO loginVO = loginDao.getLoginInfo(paramMap);

		if(ObjectUtils.isEmpty(loginVO)){
            throw new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다.");
		}
        
        loginVO.setRoles(Arrays.asList("ROLE_USER")); //추후 권한 정보 설정
        
        return loginVO;
	}

    public Map<String,Object> login(Map<String,Object> paramMap, HttpServletResponse response) throws Exception{
        Authentication authentication = 
            authenticationManagerBuilder
                .getObject()
                .authenticate(new UsernamePasswordAuthenticationToken(paramMap.get("email"), paramMap.get("password")) );
        return SecurityUtil.setLoginHeader(tokenProvider.generateToken(authentication), response);
    }

    public Map<String,Object> refreshToken(Map<String,Object> paramMap, HttpServletResponse response) throws Exception{
        if(!tokenProvider.validateToken(paramMap.get("refreshToken").toString())){
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(paramMap.get("accessToken").toString());
        return SecurityUtil.setLoginHeader(tokenProvider.generateToken(authentication), response);
    }
    
}
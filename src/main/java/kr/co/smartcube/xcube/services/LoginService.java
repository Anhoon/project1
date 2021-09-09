package kr.co.smartcube.xcube.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.smartcube.xcube.common.security.jwt.TokenProvider;
import kr.co.smartcube.xcube.mybatis.dao.LoginDao;
import kr.co.smartcube.xcube.util.Util;
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
		Map<String, Object> login = loginDao.selectLoginInfo(username);
		if(login != null){
			return new User((String)login.get("email"), (String)login.get("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		}else{
			throw new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다. ");
		}
	}

    public Map<String, Object> selectLoginInfo(Map<String, Object> paramMap) throws UsernameNotFoundException {
		return loginDao.selectLoginInfo(paramMap);
	}

    @Transactional
    public Map<String,Object> login(Map<String,Object> paramMap) throws Exception{
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(new UsernamePasswordAuthenticationToken(paramMap.get("email"), paramMap.get("password")) );
        Map<String,Object> token = tokenProvider.generateToken(authentication);
        //updateToken(token);
        return token;
    }

    @Transactional
    public Map<String,Object> refreshToken(Map<String,Object> paramMap) throws Exception{
        if(!tokenProvider.validateToken((String)paramMap.get("refreshToken"))){
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication((String)paramMap.get("accessToken"));
        Map<String, Object> login = loginDao.selectLoginInfo((authentication.getName()));

        if(Util.isEmpty(login)){
            throw new RuntimeException("사용자 정보가 없습니다.");
        }

        /*
        if (!login.get("refreshToken").equals((String)paramMap.get("refreshToken"))) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
        */

        Map<String,Object> token = tokenProvider.generateToken(authentication);
        //updateToken(token);
        return token;
    }

    @Transactional    
    public int updateToken(Map<String,Object> paramMap) throws Exception{
        return loginDao.updateToken(paramMap);
    }


    @Transactional
    public int logout(Map<String,Object> paramMap) throws Exception{
        return loginDao.deleteToken(paramMap);
    }
    
}
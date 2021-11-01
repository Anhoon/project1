package kr.co.springboot.project.util;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

import kr.co.springboot.project.common.security.jwt.JwtFilter;

public class SecurityUtil {
    // SecurityContext 에 유저 정보가 저장되는 시점
    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    public static String getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw  new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }

        return authentication.getName();
    }

    public static Map<String,Object> setLoginHeader(Map<String,Object> loginInfo, HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        response.addHeader(JwtFilter.AUTHORIZATION_HEADER, "");

        if(!ObjectUtils.isEmpty(loginInfo) && !ObjectUtils.isEmpty(Util.objToMap(loginInfo.get("token")))) {
            cookie = new Cookie("refreshToken", Util.objToMap(loginInfo.get("token")).get("refreshToken").toString());
            response.setHeader(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_PREFIX + Util.objToMap(loginInfo.get("token")).get("accessToken").toString());

            Util.objToMap(loginInfo.get("token")).remove("grantType");
            Util.objToMap(loginInfo.get("token")).remove("refreshToken");
            Util.objToMap(loginInfo.get("token")).remove("refreshTokenExpiresIn");
        }

        if(!ObjectUtils.isEmpty(loginInfo) && !ObjectUtils.isEmpty(Util.objToMap(loginInfo.get("user")))) {
            Util.objToMap(loginInfo.get("user")).remove("obid");
            Util.objToMap(loginInfo.get("user")).remove("password");
            Util.objToMap(loginInfo.get("user")).remove("roles");
        }

        cookie.setPath("/api");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        
        return loginInfo;
    }
}
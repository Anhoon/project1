package kr.co.springboot.project.common.security.jwt;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.co.springboot.project.mybatis.dao.LoginDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String REFRESH_TOKEN_PREFIX = "refreshToken=";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    
    @Autowired
    private LoginDao loginDao;

    private final Key key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Map<String, Object> generateToken(Authentication authentication) throws Exception {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Map<String, Object> userMap = new HashMap<String, Object>();
        Map<String, Object> tokenMap = new HashMap<String, Object>();

        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        
        try {
            LoginVO loginVO = (LoginVO)authentication.getPrincipal();

            for (Field field : loginVO.getClass().getDeclaredFields()){
                field.setAccessible(true);
                Object value = field.get(loginVO);
                if(!ObjectUtils.isEmpty(value)) userMap.put(field.getName(), value);
            }
        } catch (Exception e) {
            throw new RuntimeException("사용자 정보가 없습니다.");
        }

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_USER"
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        tokenMap.put("grantType", BEARER_TYPE);
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("accessTokenExpiresIn", accessTokenExpiresIn.getTime());
        tokenMap.put("refreshToken", refreshToken);
        tokenMap.put("refreshTokenExpiresIn", refreshTokenExpiresIn.getTime());

        returnMap.put("token", tokenMap);
        returnMap.put("user", userMap);

        return returnMap;
        
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                        
        
        // UserDetails 객체를 만들어서 Authentication 리턴
        //UserDetails principal = new User(claims.getSubject(), "", authorities);

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("email", claims.getSubject());
        LoginVO login = loginDao.getLoginInfo(paramMap);
        login.setRoles(Arrays.asList("ROLE_USER")); //추후 권한 정보 설정

        return new UsernamePasswordAuthenticationToken(login, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    public String getAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        System.out.println("bearerToken : "+bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    public String getRefreshToken(HttpServletRequest request) {
        String cookie = request.getHeader("Set-Cookie");
        System.out.println("cookie : "+cookie);
        if (StringUtils.hasText(cookie) && StringUtils.hasText(cookie.split(";")[0]) && cookie.split(";")[0].startsWith(REFRESH_TOKEN_PREFIX) &&  StringUtils.hasText(cookie.split(";")[0].split("=")[0])) {
            return cookie.split(";")[0].split("=")[1];
        }
        return null;
    }
}
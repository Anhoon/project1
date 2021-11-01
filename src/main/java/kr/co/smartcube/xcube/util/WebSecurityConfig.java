package kr.co.smartcube.xcube.util;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import kr.co.smartcube.xcube.common.security.jwt.JwtAccessDeniedHandler;
import kr.co.smartcube.xcube.common.security.jwt.JwtAuthenticationEntryPoint;
import kr.co.smartcube.xcube.common.security.jwt.JwtSecurityConfig;
import kr.co.smartcube.xcube.common.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
  
     // h2 database 테스트가 원활하도록 관련 API 들은 전부 무시
     /*
     @Override
     public void configure(WebSecurity web) {
        web.ignoring()
             .antMatchers("/h2-console/**", "/favicon.ico");
     }
    */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.cors().configurationSource(corsConfigurationSource()).and()
            .csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .formLogin().disable()
            // exception handling 할 때 우리가 만든 클래스를 추가
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)

            // h2-console 을 위한 설정을 추가
            //.and()
            //.headers()
            //.frameOptions()
            //.sameOrigin()

            // 시큐리티는 기본적으로 세션을 사용
            // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
            .antMatchers("/api/test/**").permitAll()
            .antMatchers("/api/login/**/").permitAll()
            .antMatchers(HttpMethod.POST, "/api/user/").permitAll()
            .antMatchers(HttpMethod.GET, "/api/user/certified").permitAll()
            .antMatchers(HttpMethod.GET, "/api/user/certified").permitAll()
            .antMatchers(HttpMethod.GET, "/api/user/userCheck/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/user/findPassword/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/user/findUser/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/file/download/**").permitAll()
            //.anyRequest().authenticated()   // 나머지 API 는 전부 인증 필요

            // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
            .and()
            .apply(new JwtSecurityConfig(tokenProvider));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
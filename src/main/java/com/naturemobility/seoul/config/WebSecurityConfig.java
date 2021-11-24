package com.naturemobility.seoul.config;

import com.naturemobility.seoul.jwt.*;
import com.naturemobility.seoul.mapper.UsersMapper;
import com.naturemobility.seoul.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtService jwtService;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
//    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //스프링 시큐리티 룰을 무시하게 하는 URL 규칙 (이곳에 등록하면 규칙이 적용하지 않는다.)
    @Override
    public void configure(WebSecurity web)throws Exception //WebSecurity는 FilterChainProxy를 생성하는 필터
    {
        web.ignoring().antMatchers("/static/**"); // 해당 경로의 파일들은 Spring Security가 무시할 수 있도록 설정. 파일 기준은 resources/static 디렉터리
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
//                .antMatchers("/users/mypage").hasRole("MEMBER")
//                .antMatchers("/users/mypage/**").hasRole("MEMBER")
//                .antMatchers("/user/check_password").hasRole("MEMBER")
//                .antMatchers("/partner/mypage").hasRole("ADMIN")
//                .antMatchers("/partner/mypage/**").hasRole("ADMIN")
//                .antMatchers("/partner//check_password").hasRole("ADMIN")
                .antMatchers("/**").permitAll()

//                .and()
//                .formLogin()
//                .loginPage("/user/login")
//                .loginProcessingUrl("/user/login.do")
////                .failureHandler(authenticationFailureHandler)
//                .permitAll()

                .and()
//                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtService);
    }
}



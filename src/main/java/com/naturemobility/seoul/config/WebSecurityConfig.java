package com.naturemobility.seoul.config;

import com.naturemobility.seoul.jwt.*;
import com.naturemobility.seoul.mapper.UsersMapper;
import com.naturemobility.seoul.service.CustomUserDetailsService;
import com.naturemobility.seoul.utils.CookieUtil;
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
    private final CookieUtil cookieUtil;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
//    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //????????? ???????????? ?????? ???????????? ?????? URL ?????? (????????? ???????????? ????????? ???????????? ?????????.)
    @Override
    public void configure(WebSecurity web)throws Exception //WebSecurity??? FilterChainProxy??? ???????????? ??????
    {
        web.ignoring().antMatchers("/static/**"); // ?????? ????????? ???????????? Spring Security??? ????????? ??? ????????? ??????. ?????? ????????? resources/static ????????????
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // ????????? ???????????? ?????? ????????? STATELESS??? ??????
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/users/mypage").hasRole("MEMBER")
                .antMatchers("/users/mypage/**").hasRole("MEMBER")
                .antMatchers("/users/check_password").hasRole("MEMBER")
                .antMatchers("/reservation/**").hasRole("MEMBER")
                .antMatchers("/review/**").hasRole("MEMBER")
                .antMatchers("/visited/**").hasRole("MEMBER")
                .antMatchers("/users/refresh").hasRole("MEMBER")
                .antMatchers("/partner/mypage").hasRole("ADMIN")
//                .antMatchers("/partner/mypage/**").hasRole("ADMIN")
                .antMatchers("/partner/check_password").hasRole("ADMIN")
                .antMatchers("/partner/refresh").hasRole("ADMIN")
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
        return new JwtFilter(jwtService,cookieUtil);
    }
}



package com.example.security1.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //페이지 권한 설정
        http
                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/","/login", "join","joinProc").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN","USER")
                        .anyRequest().authenticated()
                );
        //로그인 처리
        http
                .formLogin((auth) -> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .defaultSuccessUrl("/")
                        .permitAll()
                );
        //csrf 처리
        //CSRF(Cross-Site Request Forgery)는 요청을 위조하여 사용자가 원하지 않아도 서버측으로 특정 요청을 강제로 보내는 방식이다.
        //security config 클래스에서 csrf.disable() 설정을 진행하지 않으면 자동으로 enable 설정이 진행된다. enable 설정시 스프링 시큐리티는 CsrfFilter를 통해 POST, PUT, DELETE 요청에 대해서 토큰 검증을 진행한다.
//        http
//                .csrf((auth) -> auth.disable());

        //세션 설정
        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        //최대 세션을 초과했을때 로그인을 허용할지(기존 로그아웃)
                        .maxSessionsPreventsLogin(true));
        //세션 고정 보호 ( 탈취시 보호 )
        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId());
        //토큰이 없으면 GET요청 못씀,
        http
                .logout((auth) -> auth.logoutUrl("/logout")
                        .logoutSuccessUrl("/"));
        return http.build();
    }
}

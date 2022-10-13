package com.lodong.spring.supermandiary.config;

import com.lodong.spring.supermandiary.jwt.JwtAuthenticationFilter;
import com.lodong.spring.supermandiary.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()//rest api이므로 basic auth 및 csrf 보안을 사용하지 않는다는 설정이다.
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//JWT를 사용하기 때문에 세션을 사용하지 않는다는 설정이다.
                .and()
                .authorizeRequests()
                .antMatchers("/rest/v1/auth/**").permitAll()
                .antMatchers("rest/v1/admin/**").hasRole("ADMIN")
                .antMatchers("/rest/v1/**").hasRole("USER") //USER 권한이 있어야 요청할 수 있다는 설정이다.
                .antMatchers("/rest/v1/**").hasRole("ADMIN") //ADMIN 권한이 있어야 요청할 수 있다는 설정이다.
                .anyRequest().authenticated() //이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정이다.
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        //JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행하겠다는 설정이다.
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

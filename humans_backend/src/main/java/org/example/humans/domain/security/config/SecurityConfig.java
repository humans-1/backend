package org.example.humans.domain.security.config;

import lombok.RequiredArgsConstructor;
import org.example.humans.domain.security.global.filter.CustomLoginFilter;
import org.example.humans.domain.security.global.filter.CustomLogoutHandler;
import org.example.humans.domain.security.global.filter.JwtAuthorizationFilter;
import org.example.humans.domain.security.jwt.JwtUtil;
import org.example.humans.domain.security.service.TokenService;
import org.example.humans.domain.security.userDetails.CustomUserDetailService;
import org.example.humans.domain.user.repository.UserRepository;
import org.example.humans.domain.user.service.command.UserCommandService;
import org.example.humans.global.config.CorsConfig;
import org.example.humans.global.util.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // 빈 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    private final UserCommandService userCommandService;
    private final CustomUserDetailService customUserDetailService;


    //인증이 필요하지 않은 url
    private final String[] allowedUrls = {
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/api/users/login", //로그인은 인증이 필요하지 않음
            "/api/users/signup", //회원가입은 인증이 필요하지 않음
            "/api/users/kakao",  //카카오 소셜로그인
            "/api/users/naver",  //네이버 소셜로그인
            "/api/auth/reissue", //토큰 재발급은 인증이 필요하지 않음
            "/auth/**",
            "/api/users/password", // 이메일 인증 후 비밀번호 변경
            "/api/email/**"       // 이메일 인증
    };

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CORS 정책 설정
        http
                .cors(cors -> cors
                        .configurationSource(CorsConfig.apiConfigurationSource()));

        // csrf 비활성화
        http
                .csrf(AbstractHttpConfigurer::disable);

        // form 로그인 방식 비활성화 -> REST API 로그인을 사용할 것이기 때문에
        http
                .formLogin(AbstractHttpConfigurer::disable);

        // http basic 인증 방식 비활성화
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        // 세션을 사용하지 않음. (세션 생성 정책을 Stateless 설정.)
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 경로별 인가
        http
                .authorizeHttpRequests(auth -> auth
                        //위에서 정의했던 allowedUrls 들은 인증이 필요하지 않음 -> permitAll
                        .requestMatchers(allowedUrls).permitAll()
                        .anyRequest().authenticated() // 그 외의 url 들은 인증이 필요함
                );

        // Login Filter
        CustomLoginFilter loginFilter = new CustomLoginFilter(
                authenticationManager(authenticationConfiguration), jwtUtil, userCommandService);



        // Login Filter URL 지정
        loginFilter.setFilterProcessesUrl("/api/users/login");

        // filter chain 에 login filter 등록
        http
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
        // login filter 전에 Auth Filter 등록
        http
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil, redisUtil, userRepository), CustomLoginFilter.class);

        http
                .logout(logout -> logout
                        .logoutUrl("/api/users/logout")
                        .addLogoutHandler(new CustomLogoutHandler(redisUtil, jwtUtil))
                        .logoutSuccessHandler((request, response, authentication) -> {
                        })
                );

        return http.build();
    }

}


package org.example.humans.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.humans.domain.Users;
import org.example.humans.dto.JwtToken;
import org.example.humans.dto.LogInDto;
import org.example.humans.dto.SignUpDto;
import org.example.humans.dto.UserDto;
import org.example.humans.repository.UserRepository;
import org.example.humans.service.UserService;
import org.example.humans.util.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Slf4j
public class UserApiController {
    private final UserService userService;

    @PostMapping("/account/login")
    public ResponseEntity<?> login(@RequestBody LogInDto loginDto){
        String ID = loginDto.getId();
        String password = loginDto.getPassword();

        try {
            JwtToken jwtToken = userService.login(loginDto);
            log.info("request ID = {}, password = {}", ID, password);
            log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
            return ResponseEntity.ok(jwtToken);
        } catch (IllegalArgumentException e) {
            log.error("로그인 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    @PostMapping("/account/test")
    public String test(){
        return SecurityUtil.getCurrentUsername();
    }

    @PostMapping("/account/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignUpDto signUpDto){
        UserDto savedUserDto = userService.signUp(signUpDto);
        return ResponseEntity.ok(savedUserDto);
    }

    @GetMapping("/account/{userId}")
    public ApiResult<UserDto> findById(@PathVariable Long userId) {
        try {
            return ApiResult.succeed(userService.findById(userId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.failed(e.getMessage());
        }
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request,response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
}

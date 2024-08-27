package org.example.humans.controller;

import lombok.RequiredArgsConstructor;
import org.example.humans.dto.JwtToken;
import org.example.humans.dto.TokenRefreshRequest;
import org.example.humans.provider.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/account/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request){
        String refreshToken = request.getRefreshToken();
        try{
            JwtToken newTokens = jwtTokenProvider.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(newTokens);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 리프레시 토큰입니다.");
        }
    }
}


package org.example.humans.domain.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.humans.domain.security.dto.JwtDto;
import org.example.humans.domain.security.exception.SecurityErrorCode;
import org.example.humans.domain.security.jwt.JwtUtil;
import org.example.humans.global.apiPayload.exception.GeneralException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    public JwtDto reissueAccessToken(String refreshToken){
        log.info("[ AuthService ] 토큰 재발급을 시작합니다.");

        try {
            String email = jwtUtil.getEmail(refreshToken);
            log.info("[ AuthService ] email -> {}",email);

            jwtUtil.validateToken(refreshToken);

            if (tokenService.isTokenBlacklisted(refreshToken)){
                log.info("[ AuthService ] 블랙리스트로 등록된 토큰입니다.");
                throw new GeneralException(SecurityErrorCode.BLACKLIST_TOKEN);
            }

            String storedRefreshToken = tokenService.getRefreshTokenByEmail(email);
            if(storedRefreshToken==null || !storedRefreshToken.equals(refreshToken)){
                log.info("[ AuthService ] 저장된 refresh 토큰과 일치하지 않습니다.");
                throw new GeneralException(SecurityErrorCode.REFRESH_TOKEN_NOT_FOUND);
            }

            log.info("[ AuthService ] refresh token이 유효합니다. 새로운 access token을 발급합니다.");

            JwtDto newToken = jwtUtil.reissueToken(refreshToken);

            return newToken;
        }catch (SecurityException e){
            log.error("[ AuthService ] 보안 예외 발생: {}", e.getMessage());
            throw new SecurityException("유효하지 않은 refresh token입니다.",e);
        }
    }
}

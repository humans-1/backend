package org.example.humans.domain.security.global.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.humans.domain.security.exception.SecurityErrorCode;
import org.example.humans.domain.security.jwt.JwtUtil;
import org.example.humans.global.apiPayload.CustomResponse;
import org.example.humans.global.util.HttpResponseUtil;
import org.example.humans.global.util.RedisUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        try {
            String accessToken = jwtUtil.resolveAccessToken(request);
            log.info("accessToken pasrsing: {}",accessToken);

            if(accessToken==null){
                log.warn("[ CustomLogoutHandler ] AccessToken이 없습니다.");
                setErrorResponse(response,SecurityErrorCode.BAD_CREDENTIALS);
                return;
            }
            jwtUtil.validateToken(accessToken);

            String email = jwtUtil.getEmail(accessToken);
            String accessTokenKey = email + ":blacklist";
            log.info("accessTokenKey: {}", accessTokenKey);

            redisUtil.setBlackList(accessTokenKey,accessToken,30*60*1000L);
            log.info("[ CustomLogoutHandler ] accessToken blacklist 등록");

            String refreshTokenKey = email + ":blacklist";
            String refreshToken = (String) redisUtil.get(refreshTokenKey);
            log.info("refreshToken: {}", refreshToken);

            if (refreshToken!=null){
                redisUtil.delete(refreshTokenKey);
                log.info("[ CustomLogoutHandler ] refresh token 삭제");
            }else {
                log.warn("[ CustomLogoutHandler ] refresh token이 존재하지 않습니다.");
                setErrorResponse(response,SecurityErrorCode.REFRESH_TOKEN_NOT_FOUND);
                return;
            }

            HttpResponseUtil.setSuccessResponse(response,HttpStatus.OK,"로그아웃이 완료되었습니다");
        }catch (ExpiredJwtException e) {
            log.warn("[ CustomLogoutHandler ] Access Token 이 만료되었습니다.");
            setErrorResponse(response, SecurityErrorCode.TOKEN_EXPIRED);
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.warn("[ CustomLogoutHandler ] 유효하지 않은 토큰입니다.");
            setErrorResponse(response, SecurityErrorCode.INVALID_TOKEN);
        } catch (Exception e) {
            log.error("[ CustomLogoutHandler ] 로그아웃 처리 중 오류 발생: {}", e.getMessage());
            log.error("Exception: {}", e.getClass().getName());
            log.error("Exception: {}", e.getClass().getSimpleName());
            log.error("Exception: {}", e.getClass());
            setErrorResponse(response, SecurityErrorCode.INTERNAL_SECURITY_ERROR);
        }
    }

    private void setErrorResponse(HttpServletResponse response, SecurityErrorCode errorCode){
        try {
            CustomResponse<Void> customResponse = CustomResponse.onFailure(errorCode.getCode(), errorCode.getMessage());
            HttpResponseUtil.setErrorResponse(response,errorCode.getStatus(),customResponse);
        }catch (IOException e){
            log.error("[ CustomLogoutHandler ] 응답 처리 중 IOException 발생: {}", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}

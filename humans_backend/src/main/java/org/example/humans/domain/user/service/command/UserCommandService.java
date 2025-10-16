package org.example.humans.domain.user.service.command;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.humans.domain.security.dto.JwtDto;
import org.example.humans.domain.security.jwt.JwtUtil;
import org.example.humans.domain.security.userDetails.CustomUserDetails;
import org.example.humans.domain.user.converter.UserConverter;
import org.example.humans.domain.user.dto.request.UserReqDTO;
import org.example.humans.domain.user.dto.response.UserResDTO;
import org.example.humans.domain.user.entity.User;
import org.example.humans.domain.user.exception.UserErrorCode;
import org.example.humans.domain.user.exception.UserException;
import org.example.humans.domain.user.repository.UserRepository;
import org.example.humans.global.util.RedisUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserCommandService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    private void validateEmail(String email){
        Optional<User> _user = userRepository.findByEmail(email);
        if(_user.isPresent()){
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }
    }

    public UserResDTO.UserDetailDTO signup(UserReqDTO.SignUpDTO dto){
        validateEmail(dto.email());
        User user = UserConverter.toUser(dto,passwordEncoder);
        userRepository.save(user);
        return UserConverter.toUserDTO(user);
    }

    public void deleteUser(HttpServletRequest request, String email){
        try {
            User user = userRepository.findByEmailAndActiveTrue(email)
                    .orElseThrow(()-> new UserException(UserErrorCode.NO_USER_DATA_REGISTERED));
            user.softDelete();
            String accessToken = jwtUtil.resolveAccessToken(request);

            if (accessToken!=null){
                String accessTokenKey = email + ":blacklist";

                redisUtil.setBlackList(accessTokenKey,accessToken,30*60*1000L); //30분 동안 블랙리스트 추가
                log.info("[ UserService ] Access 토큰 블랙리스트 등록: {}",accessToken);

                String refreshTokenKey = email+":refresh";
                String refreshToken = (String) redisUtil.get(refreshTokenKey);

                if(refreshToken != null){
                    redisUtil.delete(refreshTokenKey);
                    log.info("[ UserService ] refresh token 삭제: {}",refreshToken);
                }else {
                    log.warn("[ UserService ] refresh token이 존재하지 않습니다.");
                }
            }
        }catch (Exception e){
            log.error("[ UserService ] 사용자 Soft Delete 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("회원 탈퇴 처리 중 오류가 발생했습니다.",e);
        }
    }

    @Scheduled(cron = "0 0 2 * * ?") //매일 새벽 2시에 실행
    public void performHardDelete(){
        LocalDateTime expiryDate = LocalDateTime.now().minusDays(30); //30일 전 날짜
        userRepository.findByDeletedAtBefore(expiryDate);
    }
    public void updateNickname(String email, UserReqDTO.UpdateNicknameRequestDto dto){
        User user = userRepository.findByEmailAndActiveTrue(email).orElseThrow(() -> new UserException(UserErrorCode.NO_USER_DATA_REGISTERED));
        user.updateNickname(dto.newNickname());
    }

    public void updatePassword(String email, UserReqDTO.UpdateAuthPasswordRequestDto dto){
        User user = userRepository.findByEmailAndActiveTrue(email).orElseThrow(() -> new UserException(UserErrorCode.NO_USER_DATA_REGISTERED));
        if(passwordEncoder.matches(dto.newPassword(), user.getPassword())){
            throw new UserException(UserErrorCode.PASSWORD_UNCHANGED);
        }
        if(passwordEncoder.matches(dto.oldPassword(), user.getPassword())){
            String newEncodedPassword = passwordEncoder.encode(dto.newPassword());
            user.setPassword(newEncodedPassword);
        }else{
            throw new UserException(UserErrorCode.INCORRECT_PASSWORD);
        }
    }

    public void setPassword(String email, String password){
        User user = userRepository.findByEmailAndActiveTrue(email).orElseThrow(() -> new UserException(UserErrorCode.NO_USER_DATA_REGISTERED));

        if(passwordEncoder.matches(password, user.getPassword())){
            throw new UserException(UserErrorCode.PASSWORD_UNCHANGED);
        }
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
    }

    // 토큰 생성 메서드
    public JwtDto provideToken(User user){
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        // 토큰 생성
        JwtDto jwtDto = JwtDto.builder()
                .accessToken(jwtUtil.createAccessToken(customUserDetails)) //access token 생성
                .refreshToken(jwtUtil.createRefreshToken(customUserDetails)) //refresh token 생성
                .build();
        return jwtDto;
    }

    // 30이 지나지 않은 유저 복구
    public void restoreUser(String email){
        LocalDateTime expiryDate = LocalDateTime.now().minusDays(30);
        Optional<User> user = userRepository.findRestoreUser(email, expiryDate);
        if(user.isPresent()){
            user.get().restore();
        }
    }
}

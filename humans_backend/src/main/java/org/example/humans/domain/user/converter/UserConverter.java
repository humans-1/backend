package org.example.humans.domain.user.converter;

import org.example.humans.domain.user.dto.request.UserReqDTO;
import org.example.humans.domain.user.dto.response.UserResDTO;
import org.example.humans.domain.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserConverter {
    //dto -> entity
    public static User toUser(UserReqDTO.SignUpDTO dto, PasswordEncoder passwordEncoder){
        String newPassword = passwordEncoder.encode(dto.password());
        return User.builder()
                .email(dto.email())
                .nickname(dto.nickname())
                .password(newPassword)
                .role("ROLE_USER")
                .active(true)
                .build();
    }

    //entity -> dto
    public static UserResDTO.UserDetailDTO toUserDTO(User user){
        return UserResDTO.UserDetailDTO.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }

    public static UserResDTO.LoginResDTO toLoginDTO(User user, String accessToken, String refreshToken){
        return UserResDTO.LoginResDTO.builder()
                .id(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

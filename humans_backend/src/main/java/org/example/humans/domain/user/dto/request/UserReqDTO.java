package org.example.humans.domain.user.dto.request;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserReqDTO {
    public record SignUpDTO(
            String email,
            String nickname,
            String password
    ){}

    public record LoginReqDTO(
            String email,
            String password
    ){}
    public record UpdateNicknameRequestDto(
            String newNickname
    ){
    }

    public record UpdateAuthPasswordRequestDto(
            String oldPassword,
            String newPassword
    ){
    }

    public record UpdatePasswordRequestDto(
            String email,
            String password
    ){
    }
}

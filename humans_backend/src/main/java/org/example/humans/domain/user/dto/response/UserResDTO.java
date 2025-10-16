package org.example.humans.domain.user.dto.response;

import lombok.Builder;

public class UserResDTO {
    @Builder
    public record UserDetailDTO(
            Long id,
            String nickname,
            String email
    ){}

    @Builder
    public record LoginResDTO(
            Long id,
            String accessToken,
            String refreshToken
    ){}
    @Builder
    public record tokenDTO(
            String accessToken,
            String refreshToken
    ){}
}

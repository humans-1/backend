package org.example.humans.domain.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "로그인 DTO")
public record LoginReqDto (
        @NotBlank(message = "[ERROR] 이메일 필수")
        @Schema(description = "이메일")
        String email,

        @NotBlank(message = "[ERROR] 비밀번호 필수")
        @Schema(description = "비밀번호")
        String password
){
}

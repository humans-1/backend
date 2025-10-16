package org.example.humans.domain.security.dto;

import lombok.Builder;

@Builder
public record JwtDto (
    String accessToken,
    String refreshToken
){

}

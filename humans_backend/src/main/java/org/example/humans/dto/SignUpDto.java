package org.example.humans.dto;

import lombok.*;
import org.example.humans.domain.Users;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {
    private String id;
    private String password;
    private String nickName;
    private List<String> roles = new ArrayList<>();

    public Users toEntity(String encodedPassword, List<String> roles){
        return Users.builder()
                .id(id)
                .password(encodedPassword)
                .nickName(nickName)
                .roles(roles)
                .build();
    }
}
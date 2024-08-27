package org.example.humans.dto;

import lombok.*;
import org.example.humans.domain.Users;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    private String id;
    private String password;
    private String nickName;

    static public UserDto toDtoWithoutPassword(Users users) {
        return UserDto.builder()
                .userId(users.getUserId())
                .id(users.getId())
                .nickName(users.getNickName())
                .build();
    }
    static public UserDto toDto(Users users){
        return UserDto.builder()
                .userId(users.getUserId())
                .id(users.getId())
                .password(users.getPassword())
                .nickName(users.getNickName())
                .build();
    }

    public Users toEntity(){
        return Users.builder()
                .userId(userId)
                .id(id)
                .password(password)
                .nickName(nickName)
                .build();
    }

}
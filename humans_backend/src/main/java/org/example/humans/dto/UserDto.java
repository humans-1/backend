package org.example.humans.dto;

import lombok.*;
import org.example.humans.domain.Users;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long UserID;
    private String id;
    private String password;
    private String nickName;

    static public UserDto toDtoWithoutPassword(Users users) {
        return UserDto.builder()
                .UserID(users.getUserID())
                .id(users.getID())
                .nickName(users.getNickName())
                .build();
    }
    static public UserDto toDto(Users users){
        return UserDto.builder()
                .UserID(users.getUserID())
                .id(users.getID())
                .password(users.getPassword())
                .nickName(users.getNickName())
                .build();
    }

    public Users toEntity(){
        return Users.builder()
                .UserID(UserID)
                .ID(id)
                .password(password)
                .nickName(nickName)
                .build();
    }

}

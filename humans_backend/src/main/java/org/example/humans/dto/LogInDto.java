package org.example.humans.dto;

import lombok.*;
import org.example.humans.domain.Users;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogInDto {
    private String id;
    private String password;
}


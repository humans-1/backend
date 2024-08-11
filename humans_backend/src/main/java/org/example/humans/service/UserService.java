package org.example.humans.service;

import lombok.RequiredArgsConstructor;
import org.example.humans.domain.Users;
import org.example.humans.dto.AddUserRequest;
import org.example.humans.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String save(AddUserRequest dto){
        return userRepository.save(Users.builder()
                .ID(dto.getID())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getID();
    }
}

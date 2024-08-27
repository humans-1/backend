package org.example.humans.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.humans.domain.Users;
import org.example.humans.dto.JwtToken;
import org.example.humans.dto.LogInDto;
import org.example.humans.dto.SignUpDto;
import org.example.humans.dto.UserDto;
import org.example.humans.exception.NotFoundUserException;
import org.example.humans.provider.JwtTokenProvider;
import org.example.humans.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder; // 생성자 주입을 위해 final로 선언

    @Transactional
    public JwtToken login(LogInDto logInDto) {
        // 사용자가 데이터베이스에 존재하는지 확인
        Users user = userRepository.findById(logInDto.getId())
                .orElseThrow(() -> new NotFoundUserException("잘못된 ID 또는 비밀번호입니다."));

        if (!passwordMatches(logInDto.getPassword(), user.getPassword())) {
            log.warn("로그인 실패: 잘못된 비밀번호");
            throw new IllegalArgumentException("잘못된 ID 또는 비밀번호입니다.");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(logInDto.getId(), logInDto.getPassword());

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            return jwtTokenProvider.generateToken(authentication);
        } catch (BadCredentialsException e) {
            log.warn("로그인 실패: 잘못된 ID 또는 비밀번호");
            throw new IllegalArgumentException("잘못된 ID 또는 비밀번호입니다.");
        }
    }

    @Transactional
    public UserDto signUp(SignUpDto signUpDto) {
        if (userRepository.existsById(signUpDto.getId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        Users user = signUpDto.toEntity(encodedPassword, Collections.singletonList("USER"));
        return UserDto.toDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(String.format("There is no Id : %s", userId)));

        return UserDto.toDtoWithoutPassword(user);
    }

    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}


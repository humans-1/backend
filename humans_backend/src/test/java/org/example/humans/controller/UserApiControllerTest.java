package org.example.humans.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.humans.dto.JwtToken;
import org.example.humans.dto.LogInDto;
import org.example.humans.dto.SignUpDto;
import org.example.humans.dto.UserDto;
import org.example.humans.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class UserApiControllerTest {
    @Autowired
    DatabaseCleanUp databaseCleanUp;
    @Autowired
    UserService userService;
    @Autowired
    TestRestTemplate testRestTemplate;
    @LocalServerPort
    int randomServerPort;

    private SignUpDto signUpDto;

    @BeforeEach
    void beforeEach() {
        signUpDto = SignUpDto.builder()
                .id("spring@naver.com")
                .password("12345678")
                .nickName("sp")
                .build();
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    public void signUpTest() {
        String url = "http://localhost:" + randomServerPort + "/account/signup";
        ResponseEntity<UserDto> responseEntity = testRestTemplate.postForEntity(url, signUpDto, UserDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserDto savedUserDto = responseEntity.getBody();
        assertThat(savedUserDto.getId()).isEqualTo(signUpDto.getId());
        assertTrue(userService.passwordMatches(signUpDto.getPassword(), savedUserDto.getPassword()));
        assertThat(savedUserDto.getNickName()).isEqualTo(signUpDto.getNickName());
    }


    @Test
    public void logInTest() {
        userService.signUp(signUpDto);

        LogInDto logInDto = LogInDto.builder()
                .id("spring@naver.com")
                .password("12345678").build();

        JwtToken jwtToken = userService.login(logInDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        log.info("httpHeaders = {}", httpHeaders);

        String url = "http://localhost:" + randomServerPort + "/account/test";
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(httpHeaders), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(logInDto.getId());
    }
}

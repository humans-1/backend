package org.example.humans.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.humans.domain.security.annotation.CurrentUser;
import org.example.humans.domain.security.entity.AuthUser;
import org.example.humans.domain.user.dto.request.UserReqDTO;
import org.example.humans.domain.user.dto.response.UserResDTO;
import org.example.humans.domain.user.service.command.UserCommandService;
import org.example.humans.domain.user.service.query.UserQueryService;
import org.example.humans.global.apiPayload.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "유저 관련 API")
public class UserController {
    public final UserCommandService userCommandService;
    public final UserQueryService userQueryService;

    @PostMapping("/signup")
    @Operation(method = "POST", summary = "회원가입 API", description = "회원 가입 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "CREATED"),
            @ApiResponse(responseCode = "USER409_1",description = "이미 존재하는 이메일입니다",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "COMMON500",description = "서버 내부 오류가 발생했습니다",content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    public CustomResponse<UserResDTO.UserDetailDTO> signup(
            @RequestBody UserReqDTO.SignUpDTO dto
    ){
        UserResDTO.UserDetailDTO result = userCommandService.signup(dto);
        return CustomResponse.onSuccess(HttpStatus.CREATED, result);
    }

    @PostMapping("/login")
    @Operation(method = "POST", summary = "로그인 API", description = "로그인 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "OK, 성공"),
            @ApiResponse(responseCode = "LOGIN400", description = "올바르지 않은 요청입니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "LOGIN401", description = "잘못된 정보입니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
    })
    public ResponseEntity<?> login(@RequestBody UserReqDTO.LoginReqDTO dto) {
        return null;
    }
    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "OK, 성공"),
            @ApiResponse(responseCode = "SEC404_0", description = "리프레시 토큰이 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "SEC401_0", description = "인증 자격 증명이 제공되지 않았거나 유효하지 않습니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
    })
    public ResponseEntity<?> logout() {
        return null;
    }

    @GetMapping("")
    @Operation(method = "GET", summary = "회원 정보 조회 API", description = "현재 로그인한 회원 정보 조회하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "OK, 성공"),
            @ApiResponse(responseCode = "USER404_1", description = "사용자 데이터 값이 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "SEC401_0", description = "인증 자격 증명이 제공되지 않았거나 유효하지 않습니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
    })
    public CustomResponse<UserResDTO.UserDetailDTO> getUser(@CurrentUser AuthUser authUser){
        UserResDTO.UserDetailDTO result = userQueryService.getUser(authUser.getEmail());
        return CustomResponse.onSuccess(HttpStatus.OK, result);
    }

    @PatchMapping
    @Operation(summary = "회원 탈퇴 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "OK, 성공"),
            @ApiResponse(responseCode = "USER404_1", description = "사용자 데이터 값이 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "SEC401_0", description = "인증 자격 증명이 제공되지 않았거나 유효하지 않습니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
    })
    public CustomResponse<?> deleteUser(HttpServletRequest request,
                                        @CurrentUser AuthUser authUser){
        userCommandService.deleteUser(request, authUser.getEmail());
        return CustomResponse.onSuccess(HttpStatus.OK,"성공적으로 회원이 탈퇴되었습니다.");
    }
    @PatchMapping("/nickname")
    @Operation(summary = "회원 닉네임 수정 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "OK, 성공"),
            @ApiResponse(responseCode = "USER404_1", description = "사용자 데이터 값이 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "SEC401_0", description = "인증 자격 증명이 제공되지 않았거나 유효하지 않습니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
    })
    public CustomResponse<?> updateNickname(@CurrentUser AuthUser authUser,
                                            @RequestBody UserReqDTO.UpdateNicknameRequestDto updateNicknameRequestDto) {
        userCommandService.updateNickname(authUser.getEmail(), updateNicknameRequestDto);
        return CustomResponse.onSuccess(HttpStatus.OK, "닉네임 변경이 완료되었습니다.");
    }
    @PutMapping("/password")
    @Operation(summary = "회원 비밀번호 수정 API", description = "비밀번호 찾기에서 이메일 인증 후 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "OK, 성공"),
            @ApiResponse(responseCode = "USER400_1", description = "oldPassword가 기존 비밀번호가 다릅니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "USER400_2", description = "newPassword가 기존이랑 같습니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "USER404_1", description = "사용자 데이터 값이 존재하지 않습니다.",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
    })
    public CustomResponse<?> updatePassword(@RequestBody UserReqDTO.UpdatePasswordRequestDto dto) {
        userCommandService.setPassword(dto.email(), dto.password());
        return CustomResponse.onSuccess(HttpStatus.OK, "비밀번호 변경이 완료되었습니다.");
    }

}

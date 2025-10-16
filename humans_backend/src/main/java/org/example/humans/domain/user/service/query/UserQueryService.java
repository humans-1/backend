package org.example.humans.domain.user.service.query;

import lombok.RequiredArgsConstructor;
import org.example.humans.domain.user.converter.UserConverter;
import org.example.humans.domain.user.dto.response.UserResDTO;
import org.example.humans.domain.user.entity.User;
import org.example.humans.domain.user.exception.UserErrorCode;
import org.example.humans.domain.user.exception.UserException;
import org.example.humans.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;
    public UserResDTO.UserDetailDTO getUser(String email){
        User user = userRepository.findByEmailAndActiveTrue(email).orElseThrow(()->new UserException(UserErrorCode.NO_USER_DATA_REGISTERED));
        return UserConverter.toUserDTO(user);
    }
}

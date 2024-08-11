package org.example.humans.service;

import lombok.RequiredArgsConstructor;
import org.example.humans.domain.Users;
import org.example.humans.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
//사용자 정보 가져오는 인터페이스
public class UserDetailService implements UserDetailsService {
    private  final UserRepository userRepository;
    //id로 사용자 정보 가져오는 메소드
    @Override
    public Users loadUserByUsername(String ID) {
        return userRepository.findByID(ID)
                .orElseThrow(()->new IllegalArgumentException((ID)));
    }
}

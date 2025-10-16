package org.example.humans.domain.security.userDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.humans.domain.user.entity.User;
import org.example.humans.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        log.info("[ CustomUserDetailService ] Email을 이용해 User를 검색합니다.");
        Optional<User> userEntity = userRepository.findByEmail(email);
        if(userEntity.isPresent()){
            User user = userEntity.get();
            return new CustomUserDetails(user);
        }
        throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
    }
}

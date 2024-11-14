package org.example.humans.domain.user.principal;

import lombok.RequiredArgsConstructor;
import org.example.humans.domain.user.entity.User;
import org.example.humans.domain.user.exception.UserErrorCode;
import org.example.humans.domain.user.exception.UserException;
import org.example.humans.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    // email을 이용해 사용자를 가져오기 위해 선언
    private final UserRepository userRepository;

    @Override
    // email로 PrincipalDetails(UserDetails) 객체를 가져오는 메소드, username은 email로 생각
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new UserException(UserErrorCode.NOT_FOUND));
        // MemberException과 MemberErrorCode도 한 번 작성해주세요.
        return new PrincipalDetails(user);
    }
}

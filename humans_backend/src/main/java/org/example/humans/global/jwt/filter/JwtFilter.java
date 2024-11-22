package org.example.humans.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.humans.domain.user.exception.UserErrorCode;
import org.example.humans.domain.user.exception.UserException;
import org.example.humans.domain.user.principal.PrincipalDetailsService;
import org.example.humans.global.apiPayload.CustomResponse;
import org.example.humans.global.apiPayload.code.BaseErrorCode;
import org.example.humans.global.apiPayload.exception.GeneralException;
import org.example.humans.global.jwt.util.JwtProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final PrincipalDetailsService principalDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 구현할 부분
        try {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.split(" ")[1];
                String email = jwtProvider.getEmail(token);
                UserDetails userDetails = principalDetailsService.loadUserByUsername(email);

                if (userDetails != null) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new UserException(UserErrorCode.NOT_FOUND);
                }
            }

            filterChain.doFilter(request, response);
        } catch (GeneralException e) {
            BaseErrorCode code = e.getCode();
            response.setStatus(code.getStatus().value());
            response.setContentType("application/json; charset=UTF-8");

            CustomResponse<Object> customResponse = CustomResponse.onFailure(code.getCode(), code.getMessage(), null);

            ObjectMapper om = new ObjectMapper();
            om.writeValue(response.getOutputStream(), customResponse);

        }
    }
}

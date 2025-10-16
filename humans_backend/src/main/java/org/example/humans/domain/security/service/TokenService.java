package org.example.humans.domain.security.service;

import lombok.RequiredArgsConstructor;
import org.example.humans.global.util.RedisUtil;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RedisUtil redisUtil;
    private static final String BLACKLIST_PREFIX = "blacklist:";
    private static final long BLACKLIST_TTL_MS = 2 * 24 * 3600 * 1000L; //20일

    public void addToBlacklist(String token){
        String key = BLACKLIST_PREFIX + token;
        redisUtil.save(key,true,BLACKLIST_TTL_MS, TimeUnit.MILLISECONDS);
    }

    public String getRefreshTokenByEmail(String email){
        return (String) redisUtil.get(email+":refresh");
    }

    public void deleteTokenByEmail(String email){
        redisUtil.delete(email+":refresh");
    }

    public boolean isTokenBlacklisted(String token){
        String key = BLACKLIST_PREFIX + token;
        return redisUtil.hasKey(key);
    }
}

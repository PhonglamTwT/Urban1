package com.example.Urban.service;

import com.example.Urban.dto.AccountDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TokenCacheService {

    @CachePut(value = "jwtCache", key = "#user.username")
    public String saveJwtToCache(AccountDTO user, String jwt) {
        System.out.println("Saving JWT to cache for user: {}"+ user.getUsername());
        return jwt;
    }


    @Cacheable(value = "jwtCache", key = "#user.username")
    public String getJwtFromCache(AccountDTO user) {
        System.out.println("Fetching JWT from cache for user: {}"+ user.getUsername());
        return null; // Nếu không tìm thấy
    }


    @CacheEvict(cacheNames = {"jwtCache", "refreshTokenCache"}, key = "#user.username")
    public void clearCache(AccountDTO user) {

    }
}

package com.Merlin.Inventory.Management.System.TokenBlackListing;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BlackListedTokenService {

    private BlackListedTokenRepository blackListedTokenRepository;

    public void blacklistToken(String token, LocalDateTime expiresAt) {
        BlackListedToken blacklistedToken = new BlackListedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiresAt(expiresAt);
        blackListedTokenRepository.save(blacklistedToken);
    }

    public boolean isBlacklisted(String token) {
        return blackListedTokenRepository.existsByToken(token);
    }

    // Run every midnight to clean up expired tokens
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanExpiredTokens() {
        blackListedTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}

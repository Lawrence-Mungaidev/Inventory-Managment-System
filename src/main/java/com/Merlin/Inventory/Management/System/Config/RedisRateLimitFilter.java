package com.Merlin.Inventory.Management.System.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@Order(1)
@RequiredArgsConstructor
public class RedisRateLimitFilter extends OncePerRequestFilter {

    @Autowired
    private final RedisTemplate<String, Object> redisTemplate;

    private static final int LOGIN_LIMIT = 5;
    private static final int MPESA_LIMIT = 10;
    private static final Duration WINDOW = Duration.ofMinutes(1);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();

        if (uri.contains("/api/auth/logIn")) {
            if (isRateLimited("rate:login:" + ip, LOGIN_LIMIT)) {
                sendError(response, "Too many login attempts. Please wait 1 minute.");
                return;
            }
        }

        if (uri.contains("/api/Transactions/create")) {
            if (isRateLimited("rate:mpesa:" + ip, MPESA_LIMIT)) {
                sendError(response, "Too many transaction requests. Please slow down.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isRateLimited(String key, int limit) {
        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1) {
            redisTemplate.expire(key, WINDOW);
        }

        return count != null && count > limit;
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(429);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"" + message + "\"}");
    }
}
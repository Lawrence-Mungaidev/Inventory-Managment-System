package com.Merlin.Inventory.Management.System.Config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1)
public class RateLimitFilter extends OncePerRequestFilter {


        private final Map<String, Bucket> loginBuckets = new ConcurrentHashMap<>();
        private final Map<String, Bucket> mpesaBuckets = new ConcurrentHashMap<>();

        private Bucket createLoginBucket() {
            return Bucket.builder()
                    .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1))))
                    .build();
        }

        private Bucket createMpesaBucket() {
            return Bucket.builder()
                    .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
                    .build();
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                HttpServletResponse response,
                FilterChain filterChain)
            throws ServletException, IOException {

            String ip = request.getRemoteAddr();
            String uri = request.getRequestURI();

            if (uri.contains("/api/auth/logIn")) {
                Bucket bucket = loginBuckets.computeIfAbsent(ip, k -> createLoginBucket());
                if (!bucket.tryConsume(1)) {
                    sendError(response, "Too many login attempts. Please wait 1 minute.");
                    return;
                }
            }

            if (uri.contains("/api/Transactions/create")) {
                Bucket bucket = mpesaBuckets.computeIfAbsent(ip, k -> createMpesaBucket());
                if (!bucket.tryConsume(1)) {
                    sendError(response, "Too many transaction requests. Please slow down.");
                    return;
                }
            }
        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(429);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"" + message + "\"}");
    }
}

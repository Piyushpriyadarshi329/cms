package com.contraflow.cms.security.jwt;



import com.contraflow.cms.security.AuthUser;
import jakarta.servlet.FilterChain;
import org.springframework.data.redis.core.StringRedisTemplate;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final StringRedisTemplate redisTemplate;



    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // No Bearer token -> let the request continue unauthenticated
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        try {
            // Only ACCESS tokens authenticate for resource access. A refresh token is skipped
            // here (so it can't be used on protected endpoints); it's only accepted at /auth/refresh.
            if (!jwtService.isRefreshToken(token)) {

                final String username = jwtService.extractUsername(token);
                final String jti = jwtService.extractJti(token);

                // Reject tokens whose jti was blacklisted at logout
                boolean blacklisted = jti != null
                        && Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:jwt:" + jti));

                if (!blacklisted
                        && username != null
                        && SecurityContextHolder.getContext().getAuthentication() == null
                        && !jwtService.isTokenExpired(token)) {

                    // Build the principal straight from the JWT claims — no DB lookup.
                    String role = jwtService.extractRole(token);
                    List<GrantedAuthority> authorities = role != null
                            ? List.of(new SimpleGrantedAuthority("ROLE_" + role))
                            : Collections.emptyList();

                    AuthUser principal = new AuthUser(
                            username,
                            jwtService.extractUserId(token),
                            jwtService.extractTenantId(token),
                            jwtService.extractUserType(token),
                            authorities);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    principal,
                                    null,
                                    authorities);

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Invalid / malformed / expired-signature token -> stay unauthenticated
            logger.warn("JWT validation failed: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}

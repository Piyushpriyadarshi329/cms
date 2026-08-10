package com.contraflow.cms.security.jwt;



import com.contraflow.cms.security.AuthUser;
import com.contraflow.cms.security.service.CustomUserDetailsService;
import com.contraflow.cms.security.service.TenantUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final TenantUserDetailsService tenantUserDetailsService;

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
                final String userType = jwtService.extractUserType(token);

                if (username != null
                        && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // Load from the correct table based on the token's "type" claim.
                    // Falls back to admin for legacy tokens without a type claim.
                    UserDetails userDetails = "TENANT".equals(userType)
                            ? tenantUserDetailsService.loadUserByUsername(username)
                            : userDetailsService.loadUserByUsername(username);

                    if (!jwtService.isTokenExpired(token)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities());

                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        } catch (Exception e) {
            // Invalid / malformed / expired-signature token -> stay unauthenticated
            logger.warn("JWT validation failed: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}

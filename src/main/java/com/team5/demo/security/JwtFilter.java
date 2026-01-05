package com.team5.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // Skip JWT filter for public endpoints and static resources
        return path.startsWith("/api/auth/") || 
               path.equals("/") ||
               path.equals("/register") || 
               path.equals("/login") ||
               path.equals("/about") ||
               path.equals("/register-conference") ||
               path.startsWith("/public/") ||
               path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/") ||
               path.startsWith("/webjars/");
    }

    // @Override
    // protected void doFilterInternal(
    //         HttpServletRequest request,
    //         HttpServletResponse response,
    //         FilterChain chain
    // ) throws ServletException, IOException {

    //     String authHeader = request.getHeader("Authorization");

    //     if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    //         chain.doFilter(request, response);
    //         return;
    //     }

    //     String token = authHeader.substring(7);

    //     if (!jwtUtil.validateToken(token)) {
    //         chain.doFilter(request, response);
    //         return;
    //     }

    //     String email = jwtUtil.extractEmail(token);

    //     if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

    //         UserDetails userDetails =
    //                 userDetailsService.loadUserByUsername(email);

    //         UsernamePasswordAuthenticationToken authentication =
    //                 new UsernamePasswordAuthenticationToken(
    //                         userDetails,
    //                         null,
    //                         userDetails.getAuthorities()
    //                 );

    //         authentication.setDetails(
    //                 new WebAuthenticationDetailsSource().buildDetails(request)
    //         );

    //         SecurityContextHolder.getContext().setAuthentication(authentication);
    //     }

    //     chain.doFilter(request, response);
    // }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String token = null;
<<<<<<< Updated upstream

        // Prefer Authorization header for API calls
=======
        
        // First, try to get token from Authorization header
>>>>>>> Stashed changes
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
<<<<<<< Updated upstream

        // Fallback to cookie for browser sessions
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // No token means continue without authentication
        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        // Invalid token: continue without setting auth
        if (!jwtUtil.validateToken(token)) {
            chain.doFilter(request, response);
            return;
        }

        String email = jwtUtil.extractEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(email);
                    System.out.println("UserDetails loaded: " + userDetails);
            System.out.println("UserDetails loaded: " + userDetails.getUsername());
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    }
=======
        
        // If not in header, try to get from cookie
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // If no token found, continue without authentication
        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        // Validate token
        if (!jwtUtil.validateToken(token)) {
            chain.doFilter(request, response);
            return;
        }

        String email = jwtUtil.extractEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

}
>>>>>>> Stashed changes

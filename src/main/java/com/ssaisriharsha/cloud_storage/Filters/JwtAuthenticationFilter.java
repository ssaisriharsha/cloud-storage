package com.ssaisriharsha.cloud_storage.Filters;

import com.ssaisriharsha.cloud_storage.Entities.User;
import com.ssaisriharsha.cloud_storage.Repos.UserRepo;
import com.ssaisriharsha.cloud_storage.SecurityConfig.SecureUser;
import com.ssaisriharsha.cloud_storage.Services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepo repo;
    public JwtAuthenticationFilter(JwtService jwtService, UserRepo repo) {
        this.jwtService=jwtService;
        this.repo=repo;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");
        if(!authHeader.startsWith("Bearer ")) filterChain.doFilter(request, response);
        String jwt=authHeader.substring(7);
        if(jwtService.isJwtValid(jwt)) {
            String username=jwtService.extractEmail(jwt);
            SecureUser authUser=new SecureUser(repo.findByEmail(username));
            UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}

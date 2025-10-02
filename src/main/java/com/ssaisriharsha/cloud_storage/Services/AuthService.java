package com.ssaisriharsha.cloud_storage.Services;

import com.ssaisriharsha.cloud_storage.DTOs.UserLoginDTO;
import com.ssaisriharsha.cloud_storage.Entities.User;
import com.ssaisriharsha.cloud_storage.Repos.UserRepo;
import com.ssaisriharsha.cloud_storage.SecurityConfig.Permissions;
import com.ssaisriharsha.cloud_storage.SecurityConfig.SecureUser;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtService service;
    private final PasswordEncoder encoder;
    private final UserRepo repo;
    public AuthService(JwtService service, UserRepo repo, PasswordEncoder encoder) {
        this.service=service;
        this.repo=repo;
        this.encoder=encoder;
    }

    public String login(UserLoginDTO loginDTO) {
        if(!repo.existsByEmail(loginDTO.getEmail())) throw new RuntimeException("This user doesn't exists. Please signup.");
        User u=repo.findByEmail(loginDTO.getUsername());
        if(!encoder.matches(loginDTO.getPassword(), u.getPassword())) throw new BadCredentialsException("Wrong username/password");
        String jwt=service.generateJwt(new SecureUser(u));
        return jwt;
    }

    @Transactional
    public void signup(UserLoginDTO loginDTO) {
        if(repo.existsByEmail(loginDTO.getUsername())) throw new RuntimeException("User already exists.");
        User u=new User();
        u.setUsername(loginDTO.getUsername());
        u.setPassword(encoder.encode(loginDTO.getPassword()));
        u.setEmail(loginDTO.getEmail());
        repo.save(u);
    }
}

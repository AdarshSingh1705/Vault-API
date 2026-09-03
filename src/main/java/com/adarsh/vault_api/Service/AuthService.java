package com.adarsh.vault_api.Service;

import com.adarsh.vault_api.DTO.AuthResponse;
import com.adarsh.vault_api.DTO.LoginRequest;
import com.adarsh.vault_api.DTO.RegisterRequest;
import com.adarsh.vault_api.Entity.Users;
import com.adarsh.vault_api.Exception.DuplicateEmailException;
import com.adarsh.vault_api.Exception.InvalidCredentialsException;
import com.adarsh.vault_api.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public Users register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already registered: " + request.getEmail());
        }

        String hash = passwordEncoder.encode(request.getPassword());

        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setPasswordHash(hash);
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setRole("USER");

        return userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        Optional<Users> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
        System.out.println("Loaded user: " + userDetails.getUsername());
        System.out.println("Authorities: " + userDetails.getAuthorities());

        Users user = userOptional.get();
        if (!passwordEncoder.matches(request.getPasswordHash(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, 900000L);
    }
}
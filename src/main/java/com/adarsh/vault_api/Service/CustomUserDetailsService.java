package com.adarsh.vault_api.Service;

import com.adarsh.vault_api.Entity.Users;
import com.adarsh.vault_api.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> userOptional = userRepository.findByEmail(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        Users user = userOptional.get();

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());

        return User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(List.of(authority))
                .build();
    }


}
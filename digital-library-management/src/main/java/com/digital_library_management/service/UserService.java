package com.digital_library_management.service;

import com.digital_library_management.entity.Role;
import com.digital_library_management.entity.User;
import com.digital_library_management.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(String fullName, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already in use");
        }
        String encoded = passwordEncoder.encode(rawPassword);

        User user = User.builder()
                .fullName(fullName)
                .email(email)
                .password(encoded)
                .active(true)
                .dateJoined(LocalDateTime.now())
                // assign exactly the enum constant ROLE_USER
                .roles(Collections.singleton(Role.ROLE_USER))
                .build();

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceClosedException("User not found with email: " + email));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceClosedException("User not found with id: " + id));
    }

    @Transactional
    public void updateProfile(Long userId, String newPhone, String newAddress) {
        User user = findById(userId);
        user.setPhone(newPhone);
        user.setAddress(newAddress);
        userRepository.save(user);
    }
}
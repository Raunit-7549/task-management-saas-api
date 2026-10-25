package com.shadow.taskmanagementsaasapi.service;

import com.shadow.taskmanagementsaasapi.model.User;
import com.shadow.taskmanagementsaasapi.repository.UserRepository;
import com.shadow.taskmanagementsaasapi.security.AuthRequest;
import com.shadow.taskmanagementsaasapi.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JWTService jwtService;

    private final AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    public User registerUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));

        if (user.getEmail() != null && userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("A user with this email already exists: " + user.getEmail());
        }

        return userRepository.save(user);
    }

    public String verify(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        if(authentication.isAuthenticated()){
            return jwtService.generateToken(request.getEmail());
        }

        throw new RuntimeException("Bad credentials");
    }

    public User updateUser(Long id, User updatedUser, String currentUserEmail) {
        User existingUser = getUserById(id);

        if (!existingUser.getEmail().equals(currentUserEmail)) {
            throw new SecurityException("Unauthorized: You can only edit your own account.");
        }

        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getEmail() != null) {
            if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
                if (userRepository.findByEmail(updatedUser.getEmail()) != null) {
                    throw new IllegalArgumentException("Email is already in use.");
                }
            }
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(encoder.encode(updatedUser.getPassword())); // Encrypt new password!
        }
        if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole().toUpperCase());
        }
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id, String currentUserEmail) {
        User existingUser = getUserById(id);

        if (!existingUser.getEmail().equals(currentUserEmail)) {
            throw new SecurityException("Unauthorized: You can only delete your own account.");
        }

        userRepository.deleteById(id);
    }
}
package com.example.movierental.service.impl;

import com.example.movierental.entity.User;
import com.example.movierental.exception.ResourceNotFoundException;
import com.example.movierental.repository.UserRepository;
import com.example.movierental.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail()))
            throw new IllegalArgumentException("Email already registered: " + user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override public List<User> getAllUsers() { return userRepository.findAll(); }

    @Override public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override public User updateUser(Long id, User u) {
        User existing = getUserById(id);
        existing.setFullName(u.getFullName());
        existing.setEmail(u.getEmail());
        existing.setPhone(u.getPhone());
        existing.setRole(u.getRole());
        if (u.getStatus() != null) existing.setStatus(u.getStatus());
        if (u.getPassword() != null && !u.getPassword().isBlank())
            existing.setPassword(passwordEncoder.encode(u.getPassword()));
        return userRepository.save(existing);
    }

    @Override public void deleteUser(Long id) { userRepository.delete(getUserById(id)); }
}

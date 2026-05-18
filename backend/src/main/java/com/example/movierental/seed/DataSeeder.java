package com.example.movierental.seed;

import com.example.movierental.entity.User;
import com.example.movierental.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdmin();
    }

    private void seedAdmin() {
        String adminEmail = "admin@movierental.com";
        if (userRepository.existsByEmail(adminEmail)) {
            return; // already seeded
        }
        User admin = new User();
        admin.setFullName("System Admin");
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode("Admin@123"));
        admin.setPhone("0000000000");
        admin.setRole(User.Role.ADMIN);
        admin.setStatus(User.Status.ACTIVE);
        userRepository.save(admin);
        System.out.println("==> Admin seeded: " + adminEmail + " / Admin@123");
    }
}

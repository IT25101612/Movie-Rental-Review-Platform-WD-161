package com.example.movierental.entity;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class User {

    private Long id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    private String phone;

    @NotBlank(message = "Password is required")
    private String password;

    private Role role = Role.CUSTOMER;
    private Status status = Status.ACTIVE;
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Role   { ADMIN, CUSTOMER }
    public enum Status { ACTIVE, INACTIVE }

    public User() {}

    public User(Long id, String fullName, String email, String phone,
                String password, Role role, Status status, LocalDateTime createdAt) {
        this.id = id; this.fullName = fullName; this.email = email;
        this.phone = phone; this.password = password; this.role = role;
        this.status = status; this.createdAt = createdAt;
    }

    public Long getId()                       { return id; }
    public void setId(Long id)                { this.id = id; }
    public String getFullName()               { return fullName; }
    public void setFullName(String v)         { this.fullName = v; }
    public String getEmail()                  { return email; }
    public void setEmail(String v)            { this.email = v; }
    public String getPhone()                  { return phone; }
    public void setPhone(String v)            { this.phone = v; }
    public String getPassword()               { return password; }
    public void setPassword(String v)         { this.password = v; }
    public Role getRole()                     { return role; }
    public void setRole(Role v)               { this.role = v; }
    public Status getStatus()                 { return status; }
    public void setStatus(Status v)           { this.status = v; }
    public LocalDateTime getCreatedAt()       { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}

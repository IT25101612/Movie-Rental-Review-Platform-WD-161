package com.example.movierental.auth.dto;

public class AuthResponse {

    private String token;
    private String type = "Bearer";
    private Long   id;
    private String fullName;
    private String email;
    private String role;
    private String status;

    public AuthResponse() {}

    public AuthResponse(String token, Long id, String fullName,
                        String email, String role, String status) {
        this.token    = token;
        this.id       = id;
        this.fullName = fullName;
        this.email    = email;
        this.role     = role;
        this.status   = status;
    }

    public String getToken()              { return token; }
    public void setToken(String v)        { this.token = v; }
    public String getType()               { return type; }
    public void setType(String v)         { this.type = v; }
    public Long getId()                   { return id; }
    public void setId(Long v)             { this.id = v; }
    public String getFullName()           { return fullName; }
    public void setFullName(String v)     { this.fullName = v; }
    public String getEmail()              { return email; }
    public void setEmail(String v)        { this.email = v; }
    public String getRole()               { return role; }
    public void setRole(String v)         { this.role = v; }
    public String getStatus()             { return status; }
    public void setStatus(String v)       { this.status = v; }
}

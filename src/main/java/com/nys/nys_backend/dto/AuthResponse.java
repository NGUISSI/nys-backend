package com.nys.nys_backend.dto;

public record AuthResponse(
    String message,
    String email,
    String role
) {}

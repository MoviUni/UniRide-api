package com.example.unirideapi.exception;

// Cuando no se encuentra un recurso
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
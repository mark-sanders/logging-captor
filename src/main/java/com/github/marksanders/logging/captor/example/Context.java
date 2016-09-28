package com.github.marksanders.logging.captor.example;

import java.util.UUID;

public class Context {
    private final String correlationId = UUID.randomUUID().toString();
    private final String user = "msanders";
    
    public String getCorrelationId() {
        return correlationId;
    }
    
    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Context [correlationId=" + correlationId + ", user=" + user + "]";
    }
}

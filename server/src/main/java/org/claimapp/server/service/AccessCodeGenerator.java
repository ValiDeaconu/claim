package org.claimapp.server.service;

public interface AccessCodeGenerator {
    String next();
    boolean release(String accessCode);
}

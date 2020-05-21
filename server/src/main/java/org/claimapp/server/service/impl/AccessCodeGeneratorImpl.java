package org.claimapp.server.service.impl;

import org.claimapp.server.service.AccessCodeGenerator;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
public class AccessCodeGeneratorImpl implements AccessCodeGenerator {
    private final Set<String> codesInUse;

    public AccessCodeGeneratorImpl() {
        this.codesInUse = new HashSet<>();
    }

    @Override
    public String next() {
        Random random = new Random();
        char[] randomAsciiAccessCode = new char[4];

        String randomAccessCode;
        do {
            for (int i = 0; i < 4; ++i) {
                randomAsciiAccessCode[i] = (char) ('A' + (char) random.nextInt(26));
            }

            randomAccessCode = String.valueOf(randomAsciiAccessCode);
        } while (codesInUse.contains(randomAccessCode));

        return randomAccessCode;
    }

    @Override
    public boolean release(String accessCode) {
        if (codesInUse.contains(accessCode)) {
            codesInUse.remove(accessCode);
            return true;
        }

        return false;
    }
}

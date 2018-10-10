package com.develop.mnemonic.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Angus
 */
public class Hash {

    /**
     * Generates SHA-256 digest for the given {@code input}.
     *
     * @param input The input to digest
     * @return The hash value for the given input
     * @throws RuntimeException If we couldn't find any SHA-256 provider
     */
    public static byte[] sha256(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Couldn't find a SHA-256 provider", e);
        }
    }
}

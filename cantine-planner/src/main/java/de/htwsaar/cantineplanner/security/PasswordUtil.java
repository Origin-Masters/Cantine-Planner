package de.htwsaar.cantineplanner.security;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;
    private static final int ITERATIONS = 3;
    private static final int MEMORY = 65536;
    private static final int PARALLELISM = 1;

    public static String hashPassword(String password) {
        byte[] salt = generateSalt();
        byte[] hash = hashPassword(password.toCharArray(), salt);

        return Base64.getEncoder().encodeToString(salt) + "$" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(String password, String storedHash) {
        String[] parts = storedHash.split("\\$");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] hash = Base64.getDecoder().decode(parts[1]);

        byte[] calculatedHash = hashPassword(password.toCharArray(), salt);

        return Base64.getEncoder().encodeToString(calculatedHash).equals(Base64.getEncoder().encodeToString(hash));
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static byte[] hashPassword(char[] password, byte[] salt) {
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withParallelism(PARALLELISM)
                .withMemoryAsKB(MEMORY)
                .withIterations(ITERATIONS);

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());

        byte[] hash = new byte[HASH_LENGTH];
        generator.generateBytes(password, hash);

        return hash;
    }
}
package de.htwsaar.cantineplanner.data.security;

        import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
        import org.bouncycastle.crypto.params.Argon2Parameters;

        import java.security.SecureRandom;
        import java.util.Base64;

        /**
         * Utility class for password hashing and verification using Argon2 algorithm.
         */
        public class PasswordUtil {

            private static final int SALT_LENGTH = 16;
            private static final int HASH_LENGTH = 32;
            private static final int ITERATIONS = 3;
            private static final int MEMORY = 65536;
            private static final int PARALLELISM = 1;

            /**
             * Hashes a password using Argon2 algorithm.
             *
             * @param password the password to hash
             * @return the hashed password in Base64 format, with the salt and hash separated by a '$'
             */
            public static String hashPassword(String password) {
                byte[] salt = generateSalt();
                byte[] hash = hashPassword(password.toCharArray(), salt);

                return Base64.getEncoder().encodeToString(salt) + "$" + Base64.getEncoder().encodeToString(hash);
            }

            /**
             * Verifies a password against a stored hash.
             *
             * @param password the password to verify
             * @param storedHash the stored hash in Base64 format, with the salt and hash separated by a '$'
             * @return true if the password matches the stored hash, false otherwise
             */
            public static boolean verifyPassword(String password, String storedHash) {
                String[] parts = storedHash.split("\\$");
                byte[] salt = Base64.getDecoder().decode(parts[0]);
                byte[] hash = Base64.getDecoder().decode(parts[1]);

                byte[] calculatedHash = hashPassword(password.toCharArray(), salt);

                return Base64.getEncoder().encodeToString(calculatedHash).equals(Base64.getEncoder().encodeToString(hash));
            }

            /**
             * Generates a random salt.
             *
             * @return a byte array containing the generated salt
             */
            private static byte[] generateSalt() {
                byte[] salt = new byte[SALT_LENGTH];
                new SecureRandom().nextBytes(salt);
                return salt;
            }

            /**
             * Hashes a password with a given salt using Argon2 algorithm.
             *
             * @param password the password to hash
             * @param salt the salt to use in the hashing process
             * @return a byte array containing the hashed password
             */
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
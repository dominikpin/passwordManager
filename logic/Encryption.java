package logic;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    private static final int SALT_SIZE = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_SIZE = 256;
    private static final String ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    public static String mainPasswordEncryption(char[] password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = digest.digest(new String(password).getBytes(StandardCharsets.UTF_8));
        String hashedPasswordString = bytesToHex(hashedPassword);

        return hashedPasswordString;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] generateKeyFromPassword(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_SIZE);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
        return secret.getEncoded();
    }

    public static String encryptPassword(char[] password, char[] key) throws Exception {
        byte[] salt = generateSalt();
        byte[] keyBytes = generateKeyFromPassword(key, salt);
    
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, ALGORITHM), new IvParameterSpec(new byte[16]));
        byte[] encryptedPassword = cipher.doFinal(new String(password).getBytes());
    
        byte[] result = new byte[SALT_SIZE + encryptedPassword.length];
        System.arraycopy(salt, 0, result, 0, SALT_SIZE);
        System.arraycopy(encryptedPassword, 0, result, SALT_SIZE, encryptedPassword.length);
    
        return Base64.getEncoder().encodeToString(result);
    }

    public static char[] decryptPassword(String encryptedPassword, char[] key) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedPassword);
    
        byte[] salt = Arrays.copyOfRange(encryptedBytes, 0, SALT_SIZE);
        byte[] keyBytes = generateKeyFromPassword(key, salt);
    
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, ALGORITHM), new IvParameterSpec(new byte[16]));
        byte[] decryptedPassword = cipher.doFinal(Arrays.copyOfRange(encryptedBytes, SALT_SIZE, encryptedBytes.length));
    
        return new String(decryptedPassword).toCharArray();
    }    
}

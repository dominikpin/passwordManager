package logic;

import java.util.Random;

public class StrongPasswordGen {
    private static final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String numbers = "0123456789";
    private static final String symbols = "!@#$%^&*()_+-=";

    public static String genStrongPassword(int length, boolean addLetters, boolean addNumbers, boolean addSimbols) {
        String characters = "";
        if (addLetters) {
            characters += letters;
        }
        if (addNumbers) {
            characters += numbers;
        }
        if (addSimbols) {
            characters += symbols;
        }

        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char character = characters.charAt(index);
            password.append(character);
        }

        return password.toString();
    }
}

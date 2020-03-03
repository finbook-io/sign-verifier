package io.finbook;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TextGenerator {

    private static final Map<Integer, Character> base64Map;

    static {
        base64Map = new HashMap<>();
        int i = 0;
        for (char character : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray()) {
            base64Map.put(i++, character);
        }
    }

    public static String getBase64TextFrom(String originalText) {
        StringBuilder base64Text = new StringBuilder();
        int[] bitArray;
        String suffix = "";
        if(8*originalText.length() % 6 == 0) {
            bitArray = new int[8*originalText.length()];
        } else {
            bitArray = new int[8*originalText.length() + 6];
            if((bitArray.length + 2) % 6 == 0) suffix = "=";
            if((bitArray.length - 2) % 6 == 0) suffix = "==";
        }

        int i = 0;
        for (char character : originalText.toCharArray()) {
            int j = i;
            for (int bit : Objects.requireNonNull(getBitsFrom(character))) {
                bitArray[j++] = bit;
            }
            i += 8;
        }

        int[] bitArrayCut = new int[6];
        i = 0;
        for (int j = 0; j < bitArray.length; j++) {
            bitArrayCut[i++] = bitArray[j];
            if((j + 1) % 6 == 0) {
                base64Text.append(base64Map.get(getIntFrom(bitArrayCut)));
                i = 0;
            }
        }

        return String.valueOf(base64Text.append(suffix));
    }

    public static String generateRandomText() {
        return getBase64TextFrom(getBase64TextFrom(String.valueOf(100000 * Math.random())));
    }

    public static int[] getBitsFrom(int number) {
        if(number >= 0 && number <= 255) {
            int[] bits = new int[8];
            int i = 7;
            while(i >= 0) {
                bits[i] = number % 2;
                number = number / 2;
                i--;
            }
            return bits;
        } else {
            return null;
        }
    }

    public static int getIntFrom(int[] bitArray) {
        int result = 0;

        for (int j = bitArray.length - 1; j >= 0; j--) {
            if(bitArray[j] == 1) {
                result += Math.pow(2, bitArray.length - j - 1);
            }
        }

        return result;
    }
}

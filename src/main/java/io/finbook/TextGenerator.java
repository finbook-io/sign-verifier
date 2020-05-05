package io.finbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextGenerator {

    private static final List<Character> base64Map;

    static {
        base64Map = new ArrayList<>();
        for (char character : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray()) {
            base64Map.add(character);
        }
    }

    public static String generateRandomText() {
        return getBase64TextFrom(String.valueOf(100000 * Math.random()));
    }

    public static String getBase64TextFrom(String asciiText) {
        int[] bitArray = getBitArrayFromAsciiText(asciiText);
        return getBase64TextFromBitArray(bitArray);
    }

    public static String getAsciiTextFrom(String base64Text) {
        int[] bitArray = getBitArrayFromBase64Text(base64Text);
        return getAsciiTextFromBitArray(bitArray);
    }

    private static String getAsciiTextFromBitArray(int[] bitArray) {
        StringBuilder asciiText = new StringBuilder();

        int[] subBitArray = new int[8];
        for (int i = 0; i < bitArray.length; i++) {
            subBitArray[i % 8] = bitArray[i];
            if((i + 1) % 8 == 0) {
                asciiText.append((char) getIntFrom(subBitArray));
            }
        }

        return String.valueOf(asciiText);
    }

    private static String getBase64TextFromBitArray(int[] bitArray) {
        StringBuilder base64Text = new StringBuilder();
        String suffix = getSuffixFrom(bitArray);

        int[] subBitArray = new int[6];
        for (int i = 0; i < bitArray.length; i++) {
            subBitArray[i % 6] = bitArray[i];
            if((i + 1) % 6 == 0) {
                base64Text.append(base64Map.get(getIntFrom(subBitArray)));
            }
        }

        return String.valueOf(base64Text.append(suffix));
    }

    private static int[] getBitArrayFromAsciiText(String asciiText) {
        int[] bitArray;
        if((asciiText.length() * 8) % 6 == 0) {
            bitArray = new int[asciiText.length() * 8];
        } else {
            bitArray = new int[asciiText.length() * 8 + 6];
        }
        for (int i = 0; i < asciiText.length(); i++) {
            int[] characterBits = getBitsFrom(asciiText.charAt(i));
            if(characterBits != null) {
                System.arraycopy(characterBits, 0, bitArray, 8 * i, characterBits.length);
            }
        }
        return bitArray;
    }

    private static int[] getBitArrayFromBase64Text(String base64Text) {
        String base64TextWithoutSuffix = getBase64TextWithoutSuffix(base64Text);

        int[] bitArray = new int[base64TextWithoutSuffix.length() * 6];
        for (int i = 0; i < base64TextWithoutSuffix.length(); i++) {
            int key = getKeyFrom(base64TextWithoutSuffix.charAt(i));
            if(key != -1) {
                int[] characterBits = getBitsFrom(key);
                if (characterBits != null) {
                    for (int j = 2; j < characterBits.length; j++) {
                        bitArray[6 * i + j - 2] = characterBits[j];
                    }
                }
            }
        }
        return bitArray;
    }

    private static String getSuffixFrom(int[] bitArray) {
        if((bitArray.length + 2) % 6 == 0) return "=";
        if((bitArray.length - 2) % 6 == 0) return "==";
        return "";
    }

    private static String getBase64TextWithoutSuffix(String base64Text) {
        for (int i = 0; i < base64Text.length(); i++) {
            if(base64Text.charAt(i) == '=') {
                return base64Text.substring(0, i);
            }
        }
        return base64Text;
    }

    private static int getKeyFrom(char character) {
        for (Character characterElement : base64Map) {
            if(characterElement.equals(character)) return base64Map.indexOf(characterElement);
        }
        return -1;
    }

    private static int[] getBitsFrom(int number) {
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

    private static int getIntFrom(int[] bitArray) {
        int result = 0;

        for (int j = bitArray.length - 1; j >= 0; j--) {
            if(bitArray[j] == 1) {
                result += Math.pow(2, bitArray.length - j - 1);
            }
        }

        return result;
    }
}

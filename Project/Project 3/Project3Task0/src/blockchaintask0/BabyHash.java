package blockchaintask0;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class BabyHash {
    public BabyHash() {
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println("Enter some data for a small hash generation");
        System.out.println("For BabyHash, all input data is converted to lower case");
        Scanner sc = new Scanner(System.in);
        String inputString = sc.nextLine();
        inputString = inputString.toLowerCase();
        String hash = ComputeSHA_256_as_Hex_String(inputString);
        System.out.println("The following is the real SHA-256 of " + inputString);
        System.out.println(hash);
        System.out.println("The following is the leftmost 2 bytes (leftmost four nibbles of SHA_256) in hex:");
        String babyHash = hash.substring(0, 4);
        System.out.println(babyHash.toUpperCase());
    }

    public static String ComputeSHA_256_as_Hex_String(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(text.getBytes("UTF-8"), 0, text.length());
            byte[] hashBytes = digest.digest();
            return convertToHex(hashBytes);
        } catch (NoSuchAlgorithmException var3) {
            System.out.println("No such algorithm exception thrown " + var3);
        } catch (UnsupportedEncodingException var4) {
            System.out.println("Unsupported encoding exception thrown " + var4);
        }

        return null;
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();

        for(int i = 0; i < data.length; ++i) {
            int halfbyte = data[i] >>> 4 & 15;
            int var4 = 0;

            do {
                if (0 <= halfbyte && halfbyte <= 9) {
                    buf.append((char)(48 + halfbyte));
                } else {
                    buf.append((char)(97 + (halfbyte - 10)));
                }

                halfbyte = data[i] & 15;
            } while(var4++ < 1);
        }

        return buf.toString();
    }
}

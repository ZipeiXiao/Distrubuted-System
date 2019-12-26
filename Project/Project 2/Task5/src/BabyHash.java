// BabyHash is a program to read any input and compute a SHA-256 hash.
// The program then displays 4 hex-digits taken from the 
// leftmost 2 bytes of the SHA-256 Hash.
// This is no longer SHA-256, but is the BabyHash.

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

/**
 *
 * @author mm6
 */
public class BabyHash {

    public static void main(String[] args)throws NoSuchAlgorithmException, UnsupportedEncodingException {

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
             // Create a SHA256 digest
             MessageDigest digest;
             digest = MessageDigest.getInstance("SHA-256");
             // allocate room for the result of the hash
             byte[] hashBytes;
             // perform the hash
             digest.update(text.getBytes("UTF-8"), 0, text.length());
             // collect result
             hashBytes = digest.digest();
             return convertToHex(hashBytes);
        }
        catch (NoSuchAlgorithmException nsa) {
            System.out.println("No such algorithm exception thrown " + nsa);
        }
        catch (UnsupportedEncodingException uee ) {
            System.out.println("Unsupported encoding exception thrown " + uee);
        }
        return null;
    } 
    // code from Stack overflow
    // converts a byte array to a string.
    // each nibble (4 bits) of the byte array is represented 
    // by a hex characer (0,1,2,3,...,9,a,b,c,d,e,f)
    private static String convertToHex(byte[] data) { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 
}

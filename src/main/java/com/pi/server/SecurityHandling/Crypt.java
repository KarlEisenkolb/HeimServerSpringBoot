package com.pi.server.SecurityHandling;

import java.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypt {

    final public static int CRYPT_USE_DEFAULT_KEY = 0;
    final public static int CRYPT_USE_PASSPHRASE = 1;

    final String TAG = "Crypt.Class";
    private final String firebaseStringKey_ORGANISATIONSAPP = "3I+nDv6ll5GhB/FRFlH2HI9ycjCDmzEv50ix9uGwA9o="; // Hier neuen Key einfügen
    private SecretKey firebaseKey;
    private static SecretKey firebasePassphraseKey;

    public static void initializePassphrase(String passphrase) {
        try {
            // byte-Array erzeugen
            byte[] key = passphrase.getBytes("UTF-8");
            // aus dem Array einen Hash-Wert erzeugen mit MD5 oder SHA
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            // nur die ersten 128 bit nutzen
            key = Arrays.copyOf(key, 32);
            // der fertige Schluessel
            firebasePassphraseKey = new SecretKeySpec(key, "AES");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("Crypt: No Type defined means CRASH!");
        }
    }

    public Crypt(int type) {

        /*KeyGenerator keygen;
        try {
            keygen = KeyGenerator.getInstance("AES");
            keygen.init(256);
            SecretKey key = keygen.generateKey();
            String stringToCopyKey = Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
            Log.d("GeneratedKey", "Crypt Constructor, keyToString: " +stringToCopyKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/

        if (type == CRYPT_USE_DEFAULT_KEY) {
            byte[] encodedKey = Base64.getDecoder().decode(firebaseStringKey_ORGANISATIONSAPP);
            firebaseKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        }else if (type == CRYPT_USE_PASSPHRASE){
            firebaseKey = firebasePassphraseKey;
        }else{
            System.out.println(TAG + "Crypt: No Type defined means CRASH!");
        }

    }

    private String encryptMain(String string){
        String encryptedString = "en failed!";
        try {
            Cipher cipher_en = Cipher.getInstance("AES/GCM/NoPadding");
            cipher_en.init(Cipher.ENCRYPT_MODE, firebaseKey);

            byte[] cipherText = cipher_en.doFinal(string.getBytes("UTF-8"));
            byte[] iv = cipher_en.getIV();

            String cipherTextString = Base64.getEncoder().encodeToString(cipherText);
            String ivString = Base64.getEncoder().encodeToString(iv);

            //System.out.println(TAG + "encryptString cipherTextString: "+cipherTextString);
            //System.out.println(TAG + "encryptString ivString: "+ivString);

            encryptedString = ivString+cipherTextString;

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println(TAG + "encryptString: Try-Catch Failed!");
        }
        return encryptedString;
    }

    private String decryptMain(String string){
        String decryptedString = "de failed!";
        try {
            Cipher cipher_de = Cipher.getInstance("AES/GCM/NoPadding");

            string = string.replace("\n" ,""); // Weiß der Geier warum da /n drin sind...
            String cipherTextString = string.substring(16);
            String ivString = string.substring(0,16);

            //System.out.println(TAG + "decryptString cipherTextString: "+cipherTextString);
            //System.out.println(TAG + "decryptString ivString: "+ivString);

            byte[] cipherText = Base64.getDecoder().decode(cipherTextString.getBytes("UTF-8"));
            byte[] iv = Base64.getDecoder().decode(ivString.getBytes("UTF-8"));

            final GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher_de.init(Cipher.DECRYPT_MODE, firebaseKey, spec);
            byte[] decryptedStringBytes = cipher_de.doFinal(cipherText);
            decryptedString = new String( decryptedStringBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println(TAG + "Crypt.Class.FAIL decryptString: Try-Catch Failed!: " +e.getMessage());
        }
        return decryptedString;
    }

    public String encryptString(String string){
        return encryptMain(string);
    }

    public String decryptString(String string){
        return decryptMain(string);
    }

    public String encryptLong(long number){
        return encryptMain(String.valueOf(number));
    }

    public long decryptLong(String number){
        return Long.valueOf(decryptMain(number));
    }

    public String encryptDouble(double number){
        return encryptMain(String.valueOf(number));
    }

    public double decryptDouble(String number){
        return Double.valueOf(decryptMain(number));
    }
}

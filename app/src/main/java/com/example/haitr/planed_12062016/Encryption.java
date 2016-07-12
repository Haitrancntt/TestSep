package com.example.haitr.planed_12062016;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by haitr on 7/11/2016.
 */
public class Encryption {
    /* public static byte[] generateKey(String password) throws Exception {
         byte[] keyStart = password.getBytes();
         KeyGenerator kgen = KeyGenerator.getInstance("AES");
         SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
         sr.setSeed(keyStart);
         kgen.init(128, sr);
         SecretKey skey = kgen.generateKey();
         return skey.getEncoded();
     }

     //Hàm thực hiện việc mã hóa dữ liệu từ 1 key
     public static byte[] encodeFile(byte[] key, byte[] fileData) throws Exception {
         SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
         Cipher cipher = Cipher.getInstance("AES");
         cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
         byte[] encrypted = cipher.doFinal(fileData);
         return encrypted;
     }

     //Hàm thực hiện việc giải mã dữ liệu từ 1 key
     public static byte[] decodeFile(byte[] key, byte[] fileData) throws Exception {
         SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
         Cipher cipher = Cipher.getInstance("AES");
         cipher.init(Cipher.DECRYPT_MODE, skeySpec);
         byte[] decrypted = cipher.doFinal(fileData);
         return decrypted;
     }*/
    private static MessageDigest messageDigest;

    // encryption password
    public static String Encryption(String password) {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] passBytes = password.getBytes();
            messageDigest.reset();
            byte[] digested = messageDigest.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digested.length; i++) {
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}

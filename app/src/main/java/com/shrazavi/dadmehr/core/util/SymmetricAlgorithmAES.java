package com.shrazavi.dadmehr.core.util;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricAlgorithmAES {
    private static byte[] iv = "0000000000000000".getBytes();
    public static SecretKeySpec setUpSecretKey(){
        try{
//            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
//            KeySpec spec = new PBEKeySpec(password, sr, 65536, 256);
//            SecretKey tmp = factory.generateSecret(spec);
//            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");


//            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//            keyGen.init(256); // for example
//            SecretKey secretKey = keyGen.generateKey();

            SecretKeySpec secretKeySpec = null;
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("hey this is my seed!!!".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            secretKeySpec = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
            return secretKeySpec;
        }catch (Exception e){
            return null;
        }
    }

    public static byte[] encryption(SecretKeySpec secretKeySpec, String unEncryptedText){
        try{
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = c.doFinal(unEncryptedText.getBytes());
            return encryptedBytes;
        }catch (Exception e){
            return null;
        }
    }

    public static byte[] decryption(SecretKeySpec secretKeySpec, byte[] encodedbytes){
        try{
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decodedBytes = c.doFinal(encodedbytes);
            return decodedBytes;
        }catch (Exception e){
            return null;
        }

    }
    public static String encrypt(String content, String key)  {
        try{
        byte[] input = content.getBytes("utf-8");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] thedigest = md.digest(key.getBytes("utf-8"));
        SecretKeySpec skc = new SecretKeySpec(thedigest, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skc, new IvParameterSpec(iv));
        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        return Base64.encodeToString(cipherText, Base64.NO_WRAP);
        }catch (Exception e){
            return null;
        }
    }
    private static String decrypt(String encrypted, String seed)
            throws Exception {
        byte[] keyb = seed.getBytes("utf-8");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] thedigest = md.digest(keyb);
        SecretKeySpec skey = new SecretKeySpec(thedigest, "AES");
        Cipher dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        dcipher.init(Cipher.DECRYPT_MODE, skey, new IvParameterSpec(iv));
        byte[] clearbyte = dcipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
        return new String(clearbyte);
    }

}

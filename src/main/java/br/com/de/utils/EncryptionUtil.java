package br.com.de.utils;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptionUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(EncryptionUtil.class);
    private static byte[] key = { 0x43, 0x33, 0x65, 0x12, 0x54, 0x78, 0x9, 0xF,
                                  0xE, 0xA, 0xD, 0x43, 0x22, 0x13, 0x48, 0x44 };

    public static String encrypt(String strToEncrypt) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            final String encryptedString = DatatypeConverter.printBase64Binary(cipher
                    .doFinal(strToEncrypt.getBytes()));
            return encryptedString;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.error("Erro ao tentar encriptar chave.",e);
        }

        return "";
    }

    public static String decrypt(String strToDecrypt) {
        try {
            if (strToDecrypt == null) {
                return "";
            }
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            final String decryptedString = new String(cipher.doFinal(DatatypeConverter
                    .parseBase64Binary(strToDecrypt)));
            return decryptedString;
        } catch (Exception e) {
            logger.error("Erro ao tentar decriptografar chave.",e);
        }
        return "";
    }
    /**
     * Utilizado para gerar as senhas criptografadas.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(encrypt("123456"));
    }
}

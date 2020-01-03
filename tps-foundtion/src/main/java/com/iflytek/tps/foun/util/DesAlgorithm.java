package com.iflytek.tps.foun.util;



import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/** DES 可反解加密 **/
public final class DesAlgorithm {

    public static final DesAlgorithm ALG = DesAlgorithm.create("?={!@#$%^&*}");
    private static final String ALGORITHM = "DES";
    private static int DES_TIMES = 3;
    private final Cipher encryptCipher, decryptCipher;

    /**
     * 指定密钥构造方法
     */
    private DesAlgorithm(String secret) {
        try {
            Key key = createKey(secret);
            encryptCipher = Cipher.getInstance(ALGORITHM);
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);

            decryptCipher = Cipher.getInstance(ALGORITHM);
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e){
            throw new RuntimeException("new DES algorithm error.....", e);
        }
    }

    public static DesAlgorithm create(String secret) {
        return new DesAlgorithm(secret);
    }

    /**
     * 加密字符串
     */
    public String encrypt(String strIn){
        try {
            return EncryptUtils.toHex(encrypt(strIn.getBytes()));
        } catch (Exception e){
            throw new RuntimeException("DES algorithm encrypt error.....", e);
        }
    }

    /**
     * 解密字符串
     */
    public String decrypt(String strIn) {
        try {
            return new String(decrypt(EncryptUtils.hex2Bytes(strIn)));
        } catch (Exception e){
            throw new RuntimeException("DES algorithm decrypt error.....", e);
        }
    }

    /**
     * 加密字节数组
     */
    private byte[] encrypt(byte[] bytes) throws BadPaddingException, IllegalBlockSizeException {
        byte[] b = bytes;
        for (int i = 0; i < DES_TIMES; i++) {
            b = encryptCipher.doFinal(b);
        }
        return b;
    }

    /**
     * 解密字节数组
     */
    private byte[] decrypt(byte[] bytes) throws BadPaddingException, IllegalBlockSizeException {
        byte[] b = bytes;
        for (int i = 0; i < DES_TIMES; i++) {
            b = decryptCipher.doFinal(b);
        }
        return b;
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位 
     */
    private Key createKey(String secret){
        byte[] bs = new byte[8];
        byte[] bytes = secret.getBytes();
        for (int i = 0; i < bytes.length && i < bs.length; i++) {
            bs[i] = bytes[i];
        }
        return new SecretKeySpec(bs, ALGORITHM);
    }
}
package com.iflytek.tps.foun.util;

import com.alibaba.fastjson.util.IOUtils;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotNull;
import java.security.Key;

/** AES 可反解加密 **/
public final class AesAlgorithm {
    /** 固定掩码 AES 加密 **/
    public static final AesAlgorithm ALG = AesAlgorithm.create("AB?={[!@#$%^&*]}");
    private static final String ALGORITHM = "AES";
    private static final String AES_ALG = "AES/ECB/PKCS5Padding";
    private static int AES_TIMES = 3;
    private final Cipher encryptCipher, decryptCipher;

    /**
     * 指定密钥构造方法
     */
    private AesAlgorithm(String secret) {
        try {
            Key key = createKey(secret);
            encryptCipher = Cipher.getInstance(AES_ALG);
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);

            decryptCipher = Cipher.getInstance(AES_ALG);
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e){
            throw new RuntimeException("new AES algorithm error.....", e);
        }
    }

    public static AesAlgorithm create(String secret) {
        return new AesAlgorithm(secret);
    }

    /**
     * 加密字符串
     */
    @NotNull
    public String encrypt(String strIn){
        try {
            return EncryptUtils.toHex(encrypt(strIn.getBytes(IOUtils.UTF8)));
        } catch (Exception e){
            throw new RuntimeException("AES algorithm encrypt error.....", e);
        }
    }

    /**
     * 解密字符串
     */
    @NotNull
    public String decrypt(String strIn) {
        try {
            return new String(decrypt(EncryptUtils.hex2Bytes(strIn)));
        } catch (Exception e){
            throw new RuntimeException("AES algorithm decrypt error.....", e);
        }
    }

    /**
     * 加密字节数组
     */
    private byte[] encrypt(byte[] bytes) throws BadPaddingException, IllegalBlockSizeException {
        byte[] b = bytes;
        for (int i = 0; i < AES_TIMES; i++) {
            b = encryptCipher.doFinal(b);
        }
        return b;
    }

    /**
     * 解密字节数组
     */
    private byte[] decrypt(byte[] bytes) throws BadPaddingException, IllegalBlockSizeException {
        byte[] b = bytes;
        for (int i = 0; i < AES_TIMES; i++) {
            b = decryptCipher.doFinal(b);
        }
        return b;
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为16位 不足8位时后面补0，超出8位只取前8位
     */
    private Key createKey(String secret){
        byte[] bs = new byte[16];
        byte[] bytes = secret.getBytes(IOUtils.UTF8);
        for (int i = 0; i < bytes.length && i < bs.length; i++) {
            bs[i] = bytes[i];
        }
        return new SecretKeySpec(bs, ALGORITHM);
    }
}
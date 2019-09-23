package com.cl.mybatisplusdemo.util;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;


public class AESUtil {
    private static String defaultcharset = "UTF-8";
    private static String KEY_AES = "AES";
    private static String KEY_MD5 = "MD5";
    //private static MessageDigest md5Digest;
    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AESUtil.class);

    static {

    }

    private static ThreadLocal<MessageDigest> md5DigestTL = new ThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance(KEY_MD5);
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                logger.error("", e);
                return null;
            }
        }
    };

    private static ThreadLocal<Cipher> cipherTL = new ThreadLocal<Cipher>() {
        @Override
        protected Cipher initialValue() {
            try {
                return Cipher.getInstance(KEY_AES);
            } catch (Exception e) {
                return null;
            }
        }
    };

    public static Cipher getInstance() throws NoSuchAlgorithmException, NoSuchPaddingException {
        return cipherTL.get();
    }


    public static String encrypt(String data, String key) {
        return doAES(data, key, Cipher.ENCRYPT_MODE);
    }

    public static String decrypt(String data, String key) {
        return doAES(data, key, Cipher.DECRYPT_MODE);
    }


    public static String doAES(String data, String key, int mode) {
        try {
            if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
                return null;
            }
            boolean encrypt = mode == Cipher.ENCRYPT_MODE;
            byte[] content;
            if (encrypt) {
                content = data.getBytes(defaultcharset);
            } else {
                content = Base64.decodeBase64(data);
            }


            SecretKeySpec keySpec = new SecretKeySpec(md5DigestTL.get().digest(key.getBytes(defaultcharset)), KEY_AES);
            Cipher cipher = getInstance();// 创建密码器
            if (cipher == null) {
                cipher = Cipher.getInstance(KEY_AES);
                cipher.init(mode, keySpec);// 初始化
                cipherTL.set(cipher);
            } else {
                //cipher = Cipher.getInstance(KEY_AES);
                cipher.init(mode, keySpec);// 初始化
                cipherTL.set(cipher);
            }


            byte[] result = cipher.doFinal(content);
            //            byte[] result = content;
            if (encrypt) {
                //使用URL安全编码模式
                return new String(Base64.encodeBase64URLSafe(result), defaultcharset);
            } else {
                return new String(result, defaultcharset); // 加密

            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    public static String encryptAndHex(String data, String key) {
        return doAESAndHex(data, key, Cipher.ENCRYPT_MODE);
    }

    public static String decryptAndHex(String data, String key) {
        return doAESAndHex(data, key, Cipher.DECRYPT_MODE);
    }


    public static String doAESAndHex(String data, String key, int mode) {
        try {
            if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
                return null;
            }
            boolean encrypt = mode == Cipher.ENCRYPT_MODE;
            byte[] content;
            if (encrypt) {
                content = data.getBytes(defaultcharset);
            } else {
                content = parseHexStr2Byte(data);
            }
            SecretKeySpec keySpec = new SecretKeySpec(md5DigestTL.get().digest(key.getBytes(defaultcharset)), KEY_AES);
            Cipher cipher = Cipher.getInstance(KEY_AES);// 创建密码器
            cipher.init(mode, keySpec);// 初始化
            byte[] result = cipher.doFinal(content);
            if (encrypt) {
                return parseByte2HexStr(result);
            } else {
                return new String(result, defaultcharset); // 加密

            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 将16进制转换为二进制
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static String decrypt(byte[] dataByte, byte[] keyByte, byte[] ivByte) {
        int base = 16;
        if (keyByte.length % base != 0) {
            int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
            keyByte = temp;
        }
        try {
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return result;
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("", e);
        } catch (NoSuchPaddingException e) {
            logger.error("", e);
        } catch (InvalidParameterSpecException e) {
            logger.error("", e);
        } catch (IllegalBlockSizeException e) {
            logger.error("", e);
        } catch (BadPaddingException e) {
            logger.error("", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        } catch (InvalidKeyException e) {
            logger.error("", e);
        } catch (InvalidAlgorithmParameterException e) {
            logger.error("", e);
        } catch (NoSuchProviderException e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * 测试
     * @param args
     */
    /*public static void main(String[] args) {
        String key = "key001002";
        String data = "test date哈哈密码马萨下===/<>,.;`!@#$%%^&*()";
        // 加密
        System.out.println("加密前：" + data);
        String s = encrypt(data, key);
        System.out.println("加密后：" + s);
        // 解密

        String s1 = decrypt(s, key);
        System.out.println("解密后：" + s1);
        String str = "abc";


        // 加密
        System.out.println("加密前：" + data);
        s = encryptAndHex(data, key);
        System.out.println("加密后：" + s);
        // 解密

        s1 = decryptAndHex(s, key);
        System.out.println("解密后：" + s1);

//		 System.out.println(DigestUtils.shaHex(str));

    }*/
    public static void main(String[] args) {
        String str = "18503857013";

        System.out.println(encryptAndHex(str, "123456"));
    }


}
package com.cl.mybatisplusdemo.framework;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.shardingsphere.spi.encrypt.ShardingEncryptor;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Properties;

/**
 * @author cl
 * @version V1.0
 */
@Getter
@Setter
@Slf4j
public class AesAndHexShardingEncryptor implements ShardingEncryptor {

    private static final String type = "aes_and_hex";
    private static final String AES_KEY = "aes.key.value";
    private static final int ENCRYPT_MIN_LENGTH = 20;

    private static final String DEFAULT_CHARSET = "UTF-8";
    private Properties properties = new Properties();


    @Override
    public String getType() {

        log.debug("GetType{}", type);
        return type;
    }

    /**
     * Initialize.
     */
    @Override
    public void init() {

    }

    @Override
    @SneakyThrows
    public String encrypt(final Object plaintext) {
        log.debug("加密 <{}>", plaintext);

        final Cipher encryptCipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = generateMySQLAESKey(DEFAULT_CHARSET);
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte valBytes[] = encryptCipher.doFinal(String.valueOf(plaintext).getBytes(DEFAULT_CHARSET));
        log.debug("加密中 <{}> ", valBytes);
        String result = new String(Hex.encodeHex(valBytes));
        log.debug("加密后 <{}> ", result);
        return result;
    }

    @Override
    @SneakyThrows
    public Object decrypt(final String ciphertext) {
        if (null == ciphertext) {
            return null;
        }
        if (ciphertext.length() < ENCRYPT_MIN_LENGTH) {
            return ciphertext;
        } else {
            log.debug("解密前 <{}> ", ciphertext);
            Cipher decryptCipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = generateMySQLAESKey(DEFAULT_CHARSET);
            decryptCipher.init(2, secretKeySpec);
            byte[] valBytes = decryptCipher.doFinal(Hex.decodeHex(ciphertext.toCharArray()));
            String r = new String(valBytes, DEFAULT_CHARSET);
            log.debug("解密后 <{}> ", r);
            return r;

        }
    }


    public SecretKeySpec generateMySQLAESKey(final String encoding) throws Exception {
        String aesKey = String.valueOf(properties.get(AES_KEY));
        if (StringUtils.isEmpty(aesKey)) {
            throw new RuntimeException("加密Key配置异常");
        }
        log.debug("加密KEY:{}",aesKey);
        final byte[] finalKey = new byte[16];
        int i = 0;
        for (byte b : aesKey.getBytes(encoding)) {
            finalKey[i++ % 16] ^= b;
        }
        return new SecretKeySpec(finalKey, "AES");
    }


}

package com.ls.luava.security;

import com.google.common.io.BaseEncoding;

import javax.crypto.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 
 * 
 * @author 杨志坚 Email: dib.yang@gmail.com
 * @since 0.2.0
 */
public class RSAUtil {

  private SecureRandom secrand = new SecureRandom();
  public Cipher rsaCipher;

  final String ALGORITHM = "RSA";// RSA、RSA/ECB/PKCS1Padding

  // public static String
  // Algorithm="RSA/ECB/PKCS1Padding";//RSA、RSA/ECB/PKCS1Padding

  private RSAUtil() {
  }

  public static RSAUtil create() {
    return new RSAUtil();
  }

  /**
   * 生成密钥对
   * 
   * @return KeyPair
   */
  public KeyPair generateKeyPair(KeySize keySize) {
    KeyPair keyPair = null;
    try {
      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
      // 密钥位数
      keyPairGen.initialize(keySize.KEY_SIZE);
      // 密钥对
      keyPair = keyPairGen.generateKeyPair();

      // // 公钥
      // RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
      //
      // System.out.println("Public m===>" +new
      // String(Hex.encodeHex(publicKey.getModulus().toByteArray())));
      // System.out.println("Public e===>" +new
      // String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray())));
      //
      //
      // // 私钥
      // RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
      //
      // System.out.println("Private m===>" +new
      // String(Hex.encodeHex(privateKey.getModulus().toByteArray())));
      // System.out.println("Private p===>" +new
      // String(Hex.encodeHex(privateKey.getPrivateExponent().toByteArray())));
      //
      //
      // String publicKeyString = getKeyString(publicKey);
      // System.out.println("Public KEY===>" + publicKeyString);
      //
      //
      // String privateKeyString = getKeyString(privateKey);
      // System.out.println("Private KEY===>" + privateKeyString);

    } catch (Exception e) {
      System.err.println("Exception:" + e.getMessage());
    }
    return keyPair;
  }

  public KeySize getKeySize(Key key) {
    if (key instanceof PrivateKey) {
      return getKeySize((PrivateKey) key);
    }
    if (key instanceof PublicKey) {
      return getKeySize((PublicKey) key);
    }
    return null;
  }

  public KeySize getKeySize(PrivateKey key) {
    String algorithm = key.getAlgorithm(); // 获取算法
    BigInteger prime = null;
    if (ALGORITHM.equals(algorithm)) { // 如果是RSA加密
      RSAPrivateKey keySpec = (RSAPrivateKey) key;
      prime = keySpec.getModulus();
    }
    return KeySize.getKeySize(prime.toString(2).length());
  }

  public KeySize getKeySize(PublicKey key) {
    String algorithm = key.getAlgorithm(); // 获取算法
    BigInteger prime = null;
    if (ALGORITHM.equals(algorithm)) { // 如果是RSA加密
      RSAPublicKey keySpec = (RSAPublicKey) key;
      prime = keySpec.getModulus();
    }
    return KeySize.getKeySize(prime.toString(2).length());
  }

  public PublicKey getPublicKey(String key) throws Exception {
    byte[] keyBytes;
    keyBytes = BaseEncoding.base64Url().decode(key);

    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
    PublicKey publicKey = keyFactory.generatePublic(keySpec);
    return publicKey;
  }

  public PrivateKey getPrivateKey(String key) throws Exception {
    byte[] keyBytes;
    keyBytes = BaseEncoding.base64Url().decode(key);

    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
    PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
    return privateKey;
  }

  public String getKeyString(Key key) throws Exception {
    byte[] keyBytes = key.getEncoded();
    String s = BaseEncoding.base64Url().encode(keyBytes);
    return s;
  }

  public String encode(Key key, String content) throws NoSuchPaddingException, IOException {
    byte[] data = content.getBytes("utf-8");
    return BaseEncoding.base64Url().encode(encode(key, data));
  }

  public byte[] encode(Key key, byte[] data) throws NoSuchPaddingException, IOException {
    KeySize keySize = this.getKeySize(key);
    try {
      rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
      throw e;
    }
    try {
      rsaCipher.init(Cipher.ENCRYPT_MODE, key, secrand);

      int blocks = data.length / keySize.BLOCK_SIZE;
      int lastBlockSize = data.length % keySize.BLOCK_SIZE;
      byte[] encryptedData = new byte[(lastBlockSize == 0 ? blocks : blocks + 1)
          * keySize.OUTPUT_BLOCK_SIZE];
      for (int i = 0; i < blocks; i++) {
        rsaCipher.doFinal(data, i * keySize.BLOCK_SIZE, keySize.BLOCK_SIZE, encryptedData, i
            * keySize.OUTPUT_BLOCK_SIZE);
      }
      if (lastBlockSize != 0) {
        rsaCipher.doFinal(data, blocks * keySize.BLOCK_SIZE, lastBlockSize, encryptedData, blocks
            * keySize.OUTPUT_BLOCK_SIZE);
      }

      return encryptedData;

    } catch (InvalidKeyException e) {
      e.printStackTrace();
      throw new IOException("InvalidKey");
    } catch (ShortBufferException e) {
      e.printStackTrace();
      throw new IOException("ShortBuffer");
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
      throw new IOException("IllegalBlockSize");
    } catch (BadPaddingException e) {
      e.printStackTrace();
      throw new IOException("BadPadding");
    } finally {

    }
  }

  /**
   * BASE64解码，再RSA解密
   * 
   * @param key
   * @param content
   * @return String
   * @throws NoSuchPaddingException
   * @throws IOException
   */
  public String decode(Key key, String content) throws NoSuchPaddingException, IOException {
    byte[] data = null;
    try {
      data = BaseEncoding.base64Url().decode(content);
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    return new String(decode(key, data), "UTF-8");
  }

  public byte[] decode(Key key, byte[] data) throws NoSuchPaddingException, IOException {
    KeySize keySize = this.getKeySize(key);
    try {
      rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
      throw e;
    }
    try {
      rsaCipher.init(Cipher.DECRYPT_MODE, key, secrand);
      int blocks = data.length / keySize.OUTPUT_BLOCK_SIZE;
      ByteArrayOutputStream decodedStream = new ByteArrayOutputStream(data.length);
      for (int i = 0; i < blocks; i++) {
        decodedStream.write(rsaCipher.doFinal(data, i * keySize.OUTPUT_BLOCK_SIZE,
            keySize.OUTPUT_BLOCK_SIZE));
      }
      return decodedStream.toByteArray();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
      throw new IOException("InvalidKey");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      throw new IOException("UnsupportedEncoding");
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
      throw new IOException("IllegalBlockSize");
    } catch (BadPaddingException e) {
      e.printStackTrace();
      throw new IOException("BadPadding");
    } finally {

    }
  }

  public static void main(String[] args) throws Exception {

    RSAUtil rsa = RSAUtil.create();
    // 密钥对
    //KeyPair keyPair = rsa.generateKeyPair(KeySize.K1024);

    String priKey = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAkvMGkImJkXv2PfF9f1bk7BTsJFSWGz8iO8FwM8dLF4kBCiAqc8d8bg9ruI1fy2tEqK1koccPA1bRv-lbIWpK8wIDAQABAkA1Sosr6aUJLLJtXmGLx6B3eVL2DfLt6KRqlUkyjejOnKRQ7MIoWNL03yJlsK5IzfMB_D0exZQSYvDg4gtj5_VRAiEA2C-LfYSGWPry8R72_nVOl7gX_rVk22XIyABIwmtqxrkCIQCuAzWFgr7FWvuR46ZM5eqR3JFmn5aCKOFym8KLZfFJCwIga59vv_LjtxRnMWaK666Wi61YNLM1HIwVYovRrQgwxfECIDMexDltuIeX-_HW9AMBRFEHgDuqxHeGdPzLX3K-Rw0TAiAZWIcMc7RsHPrp3nCrPkzDR1ZJwYiE6jBsv737_ZDmFw";
    String pubKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJLzBpCJiZF79j3xfX9W5OwU7CRUlhs_IjvBcDPHSxeJAQogKnPHfG4Pa7iNX8trRKitZKHHDwNW0b_pWyFqSvMCAwEAAQ";
    String s = "你是小铃铛 ExpTime=1226577284468$Pid=100013$Sid=rlpm001 你好啊!!!&&";
//    byte[] keyBytes;
//    keyBytes = Base64.decodeBase64(priKey);
//    System.out.println(priKey);
//    System.out.println(Base64.encodeBase64String(keyBytes));
//    System.out.println(com.google.common.io.BaseEncoding.base64().encode(keyBytes));
//    byte[] aa=com.google.common.io.BaseEncoding.base64Url().decode(priKey);
//    System.out.println(keyBytes.equals(aa));
    
    // priKey=keyPair.getPrivateKey();
    // pubKey=keyPair.getPublicKey();

    // 公钥加密
    String d = rsa.encode(rsa.getPublicKey(pubKey), s);
    System.out.println("公钥加密密文：" + d);

    // 私钥解密
    String c = rsa.decode(rsa.getPrivateKey(priKey), d);

    System.out.println("私钥解密明文：" + c);

    // 私钥加密
//    String e = rsa.encode(keyPair.getPrivate(), s);
//    System.out.println("私钥加密密文：" + e);

    // 公钥解密
//    String f = rsa.decode(keyPair.getPublic(), e);
//
//    System.out.println("公钥解密明文：" + f);

  }
}

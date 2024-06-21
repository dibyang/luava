package com.ls.luava.security;

import com.google.common.io.BaseEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
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
  static final Logger LOG = LoggerFactory.getLogger(RSAUtil.class);

  public static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
  public static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";
  public static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";
  public static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";
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
   */
  public KeyPair generateKeyPair() {
    return generateKeyPair(2048);
  }

  /**
   * 生成密钥对
   *
   * @return KeyPair
   */
  public KeyPair generateKeyPair(int keySize) {
    KeyPair keyPair = null;
    try {
      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
      // 密钥位数
      keyPairGen.initialize(keySize);
      // 密钥对
      keyPair = keyPairGen.generateKeyPair();



    } catch (Exception e) {
      System.err.println("Exception:" + e.getMessage());
    }
    return keyPair;
  }

  public int getKeySize(Key key) {
    if (key instanceof PrivateKey) {
      return getKeySize((PrivateKey) key);
    }
    if (key instanceof PublicKey) {
      return getKeySize((PublicKey) key);
    }
    return 0;
  }

  public int getKeySize(PrivateKey key) {
    String algorithm = key.getAlgorithm(); // 获取算法
    BigInteger prime = null;
    if (ALGORITHM.equals(algorithm)) { // 如果是RSA加密
      RSAPrivateKey keySpec = (RSAPrivateKey) key;
      prime = keySpec.getModulus();
    }
    return prime.toString(2).length();
  }

  public int getKeySize(PublicKey key) {
    String algorithm = key.getAlgorithm(); // 获取算法
    BigInteger prime = null;
    if (ALGORITHM.equals(algorithm)) { // 如果是RSA加密
      RSAPublicKey keySpec = (RSAPublicKey) key;
      prime = keySpec.getModulus();
    }
    return prime.toString(2).length();
  }

  public PublicKey getPublicKey(String key) throws Exception {
    byte[] keyBytes;
    key = getKey(key,BEGIN_PUBLIC_KEY,END_PUBLIC_KEY);
    keyBytes = BaseEncoding.base64().decode(key);

    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
    return keyFactory.generatePublic(keySpec);
  }

  private String getKey(String key,String begin,String end) {
    if(key.startsWith(begin)){
      key = key.substring(begin.length());
    }
    int index = key.indexOf(end);
    if(index>0){
      key = key.substring(0,index);
    }
    key = key.replaceAll("\n","")
        .replaceAll("\r","");
    return key;
  }

  public PrivateKey getPrivateKey(String key) throws Exception {
    byte[] keyBytes;
    key = getKey(key, BEGIN_PRIVATE_KEY, END_PRIVATE_KEY);
    keyBytes = BaseEncoding.base64().decode(key);

    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
    return keyFactory.generatePrivate(keySpec);
  }

  public String getKeyString(Key key) throws Exception {
    byte[] keyBytes = key.getEncoded();
    return BaseEncoding.base64().encode(keyBytes);
  }

  /**
   * 获取私钥，返回base64处理后的字符串
   * @param keyPair
   * @return
   */
  public String getPublicKeyWithBase64(KeyPair keyPair) {
    byte[] publicKey = keyPair.getPublic().getEncoded();
    return BaseEncoding.base64().encode(publicKey);
  }

  /**
   * 获取公钥，返回base64处理后的字符串
   * @param keyPair
   * @return
   */
  public String getPrivateKeyWithBase64(KeyPair keyPair) {
    byte[] privateKey = keyPair.getPrivate().getEncoded();
    return BaseEncoding.base64().encode(privateKey);
  }

  /**
   * 公钥加密
   *
   * @param key 密钥
   * @param data 待加密数据
   * @return byte[] 加密数据
   */
  public String encryptByPublicKey(String key, String data) throws Exception {
    PublicKey publicKey = getPublicKey(key);
    return encode(publicKey, data);
  }

  /**
   * 私钥解密
   *
   * @param key 密钥
   * @param data 待加密数据
   * @return byte[] 加密数据
   */
  public String decryptByPrivateKey(String key, String data) throws Exception {
    PrivateKey privateKey = getPrivateKey(key);
    return decode(privateKey, data);
  }

  /**
   * 私钥加密
   *
   * @param key 密钥
   * @param data 待加密数据
   * @return byte[] 加密数据
   */
  public String encryptByPrivateKey(String key, String data) throws Exception {
    PrivateKey privateKey = getPrivateKey(key);
    return encode(privateKey, data);
  }

  /**
   * 公钥解密
   *
   * @param key 密钥
   * @param data 待加密数据
   * @return byte[] 加密数据
   */
  public String decryptByPublicKey(String key, String data) throws Exception {
    PublicKey publicKey = getPublicKey(key);
    return decode(publicKey, data);
  }

  public String encode(Key key, String content) throws NoSuchPaddingException, IOException {
    byte[] data = content.getBytes("utf-8");
    return BaseEncoding.base64().encode(encode(key, data));
  }

  public byte[] encode(Key key, byte[] data) throws NoSuchPaddingException, IOException {
    try {
      rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
    } catch (NoSuchAlgorithmException e) {
      LOG.warn("", e);
    }
    try {
      rsaCipher.init(Cipher.ENCRYPT_MODE, key, secrand);
      return rsaCipher.doFinal(data);

    } catch (InvalidKeyException e) {
      throw new IOException("InvalidKey", e);
    }
    catch (IllegalBlockSizeException e) {
      throw new IOException("IllegalBlockSize", e);
    } catch (BadPaddingException e) {
      throw new IOException("BadPadding", e);
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
      data = BaseEncoding.base64().decode(content);
    } catch (Exception e1) {
      LOG.warn("", e1);
    }
    return new String(decode(key, data), StandardCharsets.UTF_8);
  }

  public byte[] decode(Key key, byte[] data) throws NoSuchPaddingException, IOException {

    try {
      rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
    } catch (NoSuchAlgorithmException e) {
      LOG.warn("", e);
    }
    try {
      rsaCipher.init(Cipher.DECRYPT_MODE, key, secrand);

      return rsaCipher.doFinal(data);
    } catch (InvalidKeyException e) {
      throw new IOException("InvalidKey", e);
    }
    catch (IllegalBlockSizeException e) {
      throw new IOException("IllegalBlockSize", e);
    } catch (BadPaddingException e) {
      throw new IOException("BadPadding", e);
    }
  }

  public static void main(String[] args) throws Exception {
    String password = "admin";
    RSAUtil rsa = RSAUtil.create();
    KeyPair keyPair = rsa.generateKeyPair();
    String gong = rsa.getPublicKeyWithBase64(keyPair);
    String si = rsa.getPrivateKeyWithBase64(keyPair);
    String jiami = rsa.encryptByPublicKey(gong, password);
    String jiemi = rsa.decryptByPrivateKey(si, jiami);
    System.out.println(jiami);
    System.out.println(jiemi);

    /*
    RSAUtil rsa = RSAUtil.create();
    // 密钥对
    //KeyPair keyPair = rsa.generateKeyPair(KeySize.K1024);

    String priKey = "-----BEGIN PRIVATE KEY-----\n" +
      "MIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQDJFU+yIKaPYQn5\n" +
      "2NLe7xF9vTE6fg67Hy5pmvq5dUOIRe5K3oZcOTqcggfGutXNFmIXWQWVah2Hq+Sm\n" +
      "WWAGg9xgJlgYwcojZhqyjjyDIqxnGoo6z2NtvlDe+xkXpthiXbVp3LgVon0lSlLh\n" +
      "SGJTIsFHaIwYDNMv2GEg+SIyBSLCy/P1DBe1S+jRyPkc3ppYMHPSx9UccqvNLKU/\n" +
      "lFAlq4MHk+PTjsiCz2wAYcEx5SgEFfuYxksmOAawZ8Ap7jkqaLDl+W7l09nWJIad\n" +
      "rprqutXO/jD96WjfjTGVRe0H8HbJP0bvnMFZfv+z5L8vR7IJhgqsOnD7SBjBKFcr\n" +
      "/g7erCNxAgMBAAECggEBALxU5ni6FeXyCArmpdHA7Yvr16CmEhv1/11PlwZ+KssG\n" +
      "XEHpGsRjoV6WdrwNn1NfZG3jpdQkOBYqy5N13/caJxwyrY46ap0u81BK4agcoIZM\n" +
      "mCyusrxFZK8DVKQPU1kF1XSaEhvJPRbg/rEbP2QICCheZd/cmyuTqJzeWcWpg74W\n" +
      "5eovbDqo8zyPo1DbjdHodPLHR0rXtLCjlGjrNab0/tQqzzSXswdG//13FnGReLq0\n" +
      "a2E1s3LZb3GSbsRwRoys8BsJ0uY+25f0di0ZiYiVh5JjkErAjaZdeS3oWTaqflOu\n" +
      "Wgg1+ZdJ9TakSY1j55jlXI8mf2+jAF/CJ/Af4ySCo0ECgYEA/e6Bfts6zcKf9XzD\n" +
      "5URk+PEUWLwIsZYPP/Af57G7TXVjzxfMPW9ziF1pqN1Be4TvVX7n1k83dsbSF5Xn\n" +
      "9BfzLptI6B0/emWCkpTdrgL2/iwfObJdGzaa50Bz2yrc+uQLYtgh3I+vVmmTecZs\n" +
      "jl823kU1osjN9thpsXJT9UtaQ9kCgYEAyribWEKiFkcKFcDVBmyiF0KPUlgVzGt9\n" +
      "LvB9d/AZxmr0sGbu27USi/ztD1CoHlaurMi1FbNLG4aQper5d0VAaKviGF5tvINy\n" +
      "QLOZFObNI1rny/CNkL3Ke7Ku+z3mQ/ybDcIwle6teOILXq9p3SpB3wv9TMsOG5u3\n" +
      "8kS3IuUv1VkCgYEA9c567CGruqI71ZcAyl56n8A+webDQ6TO/kWjnNUfSsvn4gBX\n" +
      "ZOEOJWLHdP849CiqxUgjhAEK3592n/4smszUSrlmycoGOKUq0FnqfRfBoCl10JQo\n" +
      "LL+fE1wAypejcfpuSzCNFsTAJhXs/GRnkSn6Iw877GgVeG3lYjAZtclLh4ECgYEA\n" +
      "g5B92TEdisG+DNIiLtIv/FKJO5LJSurzypPvifh8ceaWOSoEmPiSOeIDZC4ffdkZ\n" +
      "8i8sPxImi42wsM2n6705CPWMfe5C39abPtyQXB1SQ0DLMPNEnQxfrhoQMCRDHhqB\n" +
      "8tL1v0iwssRZRrEnTo8PQxe/46fg1xAfuI7aID6H5bkCgYEApQ8avlUjchGNZFIj\n" +
      "z2J/tc5wFV6tyv286YiYt/WSNmmYd9WGlsOmYQC9OfhPycKDaoroXA0zA5oGsqYB\n" +
      "YykSgVYRW6j0it0Q8O8ALeNR628cBqHF2EspC777xedSy0nJFo9VcxF4HcAfJFc+\n" +
      "NE4eTLxwL3YM1872IqelWBO+VIM=\n" +
      "-----END PRIVATE KEY-----";

    String pubKey = "-----BEGIN PUBLIC KEY-----\n" +
      "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyRVPsiCmj2EJ+djS3u8R\n" +
      "fb0xOn4Oux8uaZr6uXVDiEXuSt6GXDk6nIIHxrrVzRZiF1kFlWodh6vkpllgBoPc\n" +
      "YCZYGMHKI2Yaso48gyKsZxqKOs9jbb5Q3vsZF6bYYl21ady4FaJ9JUpS4UhiUyLB\n" +
      "R2iMGAzTL9hhIPkiMgUiwsvz9QwXtUvo0cj5HN6aWDBz0sfVHHKrzSylP5RQJauD\n" +
      "B5Pj047Igs9sAGHBMeUoBBX7mMZLJjgGsGfAKe45Kmiw5flu5dPZ1iSGna6a6rrV\n" +
      "zv4w/elo340xlUXtB/B2yT9G75zBWX7/s+S/L0eyCYYKrDpw+0gYwShXK/4O3qwj\n" +
      "cQIDAQAB\n" +
      "-----END PUBLIC KEY-----";
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
    */
  }
}

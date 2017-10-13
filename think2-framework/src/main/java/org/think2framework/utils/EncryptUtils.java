package org.think2framework.utils;

import org.think2framework.exception.SimpleException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

public class EncryptUtils {

	public static final String KEY_ALGORITHM = "AES";
	public static final String ENCODING = "UTF-8";

	/**
	 * 把inputString MD5加密
	 */
	public static String md5(String inputString) {
		return DigestUtils.md5Hex(inputString);
	}

	/**
	 * sha1加密
	 */
	public static String sha1(String inputString) {
		return DigestUtils.sha1Hex(inputString);
	}

	/**
	 * 异或加密，将inputString(16进制ascII码)与key进行一位对一位的异或进行加密、解密
	 * 
	 * @param inputString
	 *            待加密、解密字符串16进制ascII码
	 * @param key
	 *            加密key
	 * @param encoding
	 *            字符串编码
	 * @return 加密、解密字符串
	 */
	public static String xorASCII(String inputString, int[] key, String encoding) {
		int j;
		byte[] bs = new byte[inputString.length() / 2];
		for (int i = 0; i < inputString.length() / 2; i++) {
			j = i % key.length;
			Integer v = Integer.parseInt(inputString.substring(i * 2, i * 2 + 2), 16);
			bs[i] = (byte) (v.byteValue() ^ key[j]);
		}
		try {
			return new String(bs, 0, bs.length, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * AES加密
	 *
	 * @param txt
	 *            明文
	 * @param sKey
	 *            密钥
	 * @return AES加密 返回base64加密后的字符串
	 * @throws Exception
	 */
	public static String AESEncrypt(String txt, String sKey) {
		try {
			SecretKeySpec key = new SecretKeySpec(sKey.getBytes(), KEY_ALGORITHM);
			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);// 创建密码器
			byte[] byteTxt = txt.getBytes(ENCODING);
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			// AES 加密
			byte[] result = cipher.doFinal(byteTxt);
			// base64 加密
			return new Base64().encodeToString(result);
		} catch (Exception e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * AES解密
	 *
	 * @param txt
	 *            密文
	 * @param sKey
	 *            加密密钥
	 * @return 明文
	 * @throws Exception
	 *             异常
	 */
	public static String AESDecrypt(String txt, String sKey) {
		try {
			SecretKeySpec key = new SecretKeySpec(sKey.getBytes(), KEY_ALGORITHM);
			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			// 使用BASE64对密文进行解码
			byte[] encrypted = Base64.decodeBase64(txt);
			// 解密
			byte[] original = cipher.doFinal(encrypted);
			return new String(original, ENCODING);
		} catch (Exception e) {
			throw new SimpleException(e);
		}
	}

	/**
	 * 创建签名，根据时间戳和密钥将内容签名
	 *
	 * @param key
	 *            签名密钥
	 * @param timeStamp
	 *            时间戳
	 * @param value
	 *            签名内容
	 * @return 签名
	 */
	public static String createSignature(String key, String timeStamp, String value) {
		return md5("$" + key + "$" + timeStamp + "$" + value + "$");
	}

}

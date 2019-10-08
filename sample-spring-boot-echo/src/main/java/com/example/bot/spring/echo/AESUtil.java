package com.example.bot.spring.echo;

import java.net.URLEncoder;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.fet.crm.nspMicro.util.bean.AESEncryptResult;

/**
 * @description AES 加密
 * @notes Created by Kyle.Lin on 2019年3月25日
 */
public class AESUtil {
	public static final String AES = "AES";
	
	/**
	 * @description 用 AES 的方式解碼
	 * @notes Created by Kyle.Lin on 2019年3月26日
	 * @param encryptText				AES 加密後的 byte 陣列 
	 * @param keyStr 						加密的密鑰
	 * @param cipherType					加密方式詳細指定
	 * @param iv									需要的初始向量
	 */
	public static String decrypt(byte[] encryptText, String keyStr, CipherTypeAES cipherType, byte[] iv)
			throws Exception {
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		SecretKey secretKey = getSecretKeySpecByKeyString(keyStr);
		return decrypt(encryptText, secretKey, cipherType, ivParameterSpec);
	}
	
	/**
	 * @description 用 AES 的方式解碼
	 * @notes Created by Kyle.Lin on 2019年3月26日
	 * @param encryptText				AES 加密後的 byte 陣列 
	 * @param key 							加密的密鑰
	 * @param cipherType					加密方式詳細指定
	 * @param iv									需要的初始向量
	 */
	public static String decrypt(byte[] encryptText, byte[] key, CipherTypeAES cipherType, byte[] iv)
			throws Exception {
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		SecretKey secretKey = genRandomSecretKeyByte(key);
		
		return decrypt(encryptText, secretKey, cipherType, ivParameterSpec);
	}
	
	/**
	 * @description 用 AES 的方式解碼
	 * @notes Created by Kyle.Lin on 2019年3月26日
	 * @param cipherText					AES 加密後的 byte 陣列 
	 * @param secretKey 					加密的密鑰
	 * @param cipherType					加密方式詳細指定
	 * @param ivParameterSpec		需要的初始向量 Initial Vector
	 */
	private static String decrypt(byte[] encryptText, SecretKey keyStr, CipherTypeAES cipherType, IvParameterSpec ivParameterSpec)
			throws Exception {
		
		Cipher cipher = Cipher.getInstance(cipherType.getCode());
		cipher.init(Cipher.DECRYPT_MODE, keyStr, ivParameterSpec);
		
		
		byte[] decryptedText = cipher.doFinal(encryptText);
		String strDecryptedText = new String(decryptedText, Encoding.UTF8.getCode());
		
		return strDecryptedText;
	}
	
	/**
	 * @description KEY 和 IV 都亂數產的加密方式
	 * @notes Created by Kyle.Lin on 2019年3月27日
	 * @param msg							需要加密的訊息
	 * @param keySizeBits 				加密的密鑰 padding 長度
	 * @param cipherType					加密方式詳細指定
	 * @param ivSizeBits					想要亂數產的 iv 大小
	 */
	public static AESEncryptResult encrypt(String msg, int keySizeBytes, CipherTypeAES cipherType, int ivSizeBytes) throws Exception {
		SecretKey key = genRandomSecretKey(keySizeBytes*8);
		IvParameterSpec ivParameterSpec = genRandomIvParameterSpec(ivSizeBytes);
		
		return encrypt(msg, key, cipherType, ivParameterSpec);
	}
	/**
	 * @description KEY 和 IV 都亂數產的加密方式
	 * @notes Created by Kyle.Lin on 2019年3月27日
	 * @param msg					需要加密的訊息
	 * @param key 					加密的密鑰 padding 長度
	 * @param cipherType			加密方式詳細指定
	 * @param iv					想要亂數產的 iv 大小
	 */
	public static AESEncryptResult encrypt(String msg, byte[] key, CipherTypeAES cipherType, byte[] iv) throws Exception {
		SecretKey secretKey = genRandomSecretKeyByte(key);
		IvParameterSpec ivParameterSpec = genRandomIvParameterSpec(iv);
		
		return encrypt(msg, secretKey, cipherType, ivParameterSpec);
	}
	/**
	 * @description	需要初始向量的加密方式
	 * @notes Created by Kyle.Lin on 2019年3月26日
	 * @param msg					需要加密的訊息
	 * @param secretKey 			加密的密鑰
	 * @param cipherType			加密方式詳細指定
	 * @param ivParameterSpec		需要的初始向量 Initial Vector
	 */
	private static AESEncryptResult encrypt(String msg, SecretKey secretKey, CipherTypeAES cipherType, IvParameterSpec ivParameterSpec)
			throws Exception {
		AESEncryptResult result = new AESEncryptResult();
		
		Cipher cipher = Cipher.getInstance(cipherType.getCode());

		if (ivParameterSpec != null) {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
			byte[] iv = ivParameterSpec.getIV();
			result.setIv(iv);
			
		} else {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		}

		byte[] byteEncryptText = cipher.doFinal(msg.getBytes(Encoding.UTF8.getCode()));
		
		result.setSecretKeyBytes(secretKey.getEncoded());
		result.setByteEncryptText(byteEncryptText);
		
		return result;
	}
	
	/**
	 * @description 亂數產生 初始向量
	 * @notes Created by Kyle.Lin on 2019年3月27日
	 * @param ivByteSize
	 */
	private static IvParameterSpec genRandomIvParameterSpec(int ivSize) throws Exception {

		byte[] iv = new byte[ivSize];
		SecureRandom prng = new SecureRandom();
		prng.nextBytes(iv);

		return new IvParameterSpec(iv);
	}
	/**
	 * @description 設定 初始向量
	 * @notes Created by Kyle.Lin on 2019年3月27日
	 * @param ivByteSize
	 */
	private static IvParameterSpec genRandomIvParameterSpec(byte[] iv) throws Exception {
		return new IvParameterSpec(iv);
	}
	
	/**
	 * @description	亂數產生 AES 加密需要的 KEY
	 * @notes Created by Kyle.Lin on 2019年3月26日
	 * @param keySize 用多少 bits 去 padding
	 */
	private static SecretKey genRandomSecretKey(int keySize) throws Exception {

		KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
		SecureRandom randomNum = new SecureRandom();
		keyGenerator.init(keySize, randomNum);
		SecretKey secretKey = keyGenerator.generateKey();

		return secretKey;
	}
	
	/**
	 * @description	 設定AES 加密需要的 KEY
	 * @notes Created by Kyle.Lin on 2019年3月26日
	 * @param keySize 用多少 bits 去 padding
	 */
	private static SecretKey getSecretKeySpecByKeyString(String keyStr) throws Exception {
		byte[] raw = keyStr.getBytes(Encoding.UTF8.getCode());
		return genRandomSecretKeyByte(raw);
	}

	/**
	 * @description 用金鑰字串產 解密用 spec
	 * @notes Created by Kyle.Lin on 2019年3月27日
	 * @param keyStr 金鑰字串
	 */
	private static SecretKey genRandomSecretKeyByte(byte[] key) throws Exception {
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES);
		return secretKeySpec;
	}
	
	/**
	 * @description 把 byte 陣列轉換成 base64 字串
	 * @notes Created by Kyle.Lin on 2019年3月27日
	 * @param encryptedByte
	 */
	public static String toBase64String(byte[] encryptedByte) {
		Base64 base64 = new Base64();
		String base64String = base64.encodeAsString(encryptedByte);
		return base64String;
	}
	
	/**
	 * @description 把 String 轉換成 URL字串
	 * @notes Created by Kyle.Lin on 2019年3月27日
	 * @param encryptedByte
	 */
	public static String toURLString(String str) throws Exception {
		String base64String = URLEncoder.encode(str, "UTF-8");
		return base64String;
	}
	
	
	/**
	 * @description	AES 加密方式
	 * @notes Created by Kyle.Lin on 2019年3月27日
	 */
	public enum CipherTypeAES {
		AES_CBC_PKCS5_PADDING("AES/CBC/PKCS5PADDING");

		private String code;

		private CipherTypeAES(String code) {
			this.code = code;
		}
		public String getCode() {
			return code;
		}
	}
	
	/**
	 * @description 字符編碼
	 * @notes Created by Kyle.Lin on 2019年3月27日
	 */
	private enum Encoding {
		UTF8("UTF-8");

		private String code;

		private Encoding(String code) {
			this.code = code;
		}
		public String getCode() {
			return code;
		}
	}
}

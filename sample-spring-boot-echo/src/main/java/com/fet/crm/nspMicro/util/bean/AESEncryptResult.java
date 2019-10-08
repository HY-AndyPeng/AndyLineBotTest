/**
 * 
 */
package com.fet.crm.nspMicro.util.bean;

/**
 * @description AES 加密後的結果
 * @notes Created by Kyle.Lin on 2019年3月27日
 */
public class AESEncryptResult {
	private byte[] secretKeyBytes; // AES加密用金鑰
	private byte[] iv; // 初始向量
	private byte[] byteEncryptText; // AES 加密後訊息流

	public AESEncryptResult() {
		super();
	}

	public byte[] getSecretKeyBytes() {
		return secretKeyBytes;
	}

	public void setSecretKeyBytes(byte[] secretKeyBytes) {
		this.secretKeyBytes = secretKeyBytes;
	}

	public byte[] getIv() {
		return iv;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}

	public byte[] getByteEncryptText() {
		return byteEncryptText;
	}

	public void setByteEncryptText(byte[] byteEncryptText) {
		this.byteEncryptText = byteEncryptText;
	}

}

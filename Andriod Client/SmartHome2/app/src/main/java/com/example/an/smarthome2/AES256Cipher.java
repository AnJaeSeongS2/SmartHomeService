
package com.example.an.smarthome2;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import org.apache.commons.codec.binary.Base64;

//고급 암호화 표준(AES, Advanced Encryption Standard)
//암호화와 복호화 과정에서 동일한 키를 사용하는 대칭 키 알고리즘
public class AES256Cipher {

	private static volatile AES256Cipher INSTANCE;

	static String forIV   = "asdfasdfasdfasdfasdfasdfasdfasdf"; //32bit //default
	static String IV                = ""; //16bit
static byte[] keyData= {113, (byte)207, 82, (byte)165, 5, 12,(byte) 247,(byte) 225, 17,(byte) 235, (byte)145, (byte)181, (byte)244, 98, 10,(byte) 238, (byte)153, (byte)192, 70, 115, (byte)156, 65, 100, 120, (byte)175, (byte)223, (byte)245, 61, 108, 62, 87, 77};

	public static AES256Cipher getInstance(){
		if(INSTANCE==null){
			synchronized(AES256Cipher.class){
				if(INSTANCE==null)
					INSTANCE=new AES256Cipher();
			}
		}
		return INSTANCE;
	}

	private AES256Cipher(){

		IV = forIV.substring(0,16);
	}

	//암호화
	public static String AES_Encode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		IV = forIV.substring(0,16);

		SecretKey secureKey = new SecretKeySpec(keyData, "AES");

		//System.out.println(new String(secureKey.getEncoded()));//1235678890123456~~출력

		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));

		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		String enStr = new String(Base64.encodeBase64(encrypted));

		return enStr;
	}

	public static String AES_Encode(byte[] bytes) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		IV = forIV.substring(0,16);

		SecretKey secureKey = new SecretKeySpec(keyData, "AES");

		//System.out.println(new String(secureKey.getEncoded()));//1235678890123456~~출력

		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));

		byte[] encrypted = c.doFinal(bytes);
		String enStr = new String(Base64.encodeBase64(encrypted));

		return enStr;
	}

	public static byte[] AES_EncodeByte(byte[] bytes) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		IV = forIV.substring(0,16);

		SecretKey secureKey = new SecretKeySpec(keyData, "AES");

		//System.out.println(new String(secureKey.getEncoded()));//1235678890123456~~출력

		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));

		return c.doFinal(bytes);
	}

	public static byte[] AES_EncodeByte(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		IV = forIV.substring(0,16);

		SecretKey secureKey = new SecretKeySpec(keyData, "AES");

		//System.out.println(new String(secureKey.getEncoded()));//1235678890123456~~출력

		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));

		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		//String enStr = new String(Base64.encodeBase64(encrypted));

		return encrypted;
	}

	//복호화
	public static String AES_Decode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		//byte[] keyData = secretKey.getBytes();
		System.out.println(new String(keyData) );
		SecretKey secureKey = new SecretKeySpec(keyData, "AES");


		//System.out.println(new String(secureKey.getEncoded())); //1235678890123456~~출력

		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));

		byte[] byteStr = Base64.decodeBase64(str.getBytes());

		return new String(c.doFinal(byteStr),"UTF-8");
	}
	public static String AES_Decode(byte[] bytes) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		//byte[] keyData = secretKey.getBytes();
		System.out.println(new String(keyData) );
		SecretKey secureKey = new SecretKeySpec(keyData, "AES");


		//System.out.println(new String(secureKey.getEncoded())); //1235678890123456~~출력

		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));

		//byte[] byteStr = Base64.decodeBase64(str.getBytes());

		return new String(c.doFinal(bytes),"UTF-8");
	}
	public static byte[] AES_DecodeByte(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		//byte[] keyData = secretKey.getBytes();
		System.out.println(new String(keyData) );
		SecretKey secureKey = new SecretKeySpec(keyData, "AES");


		//System.out.println(new String(secureKey.getEncoded())); //1235678890123456~~출력

		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));

		byte[] byteStr = Base64.decodeBase64(str.getBytes());

		return c.doFinal(byteStr);
	}
	public static byte[] AES_DecodeByte(byte[] bytes) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		//byte[] keyData = secretKey.getBytes();
		System.out.println(new String(keyData) );
		SecretKey secureKey = new SecretKeySpec(keyData, "AES");


		//System.out.println(new String(secureKey.getEncoded())); //1235678890123456~~출력

		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));

		return c.doFinal(bytes);
	}

	//키생서
	public static byte[] generationAES256_KEY() throws NoSuchAlgorithmException{
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(256);
		SecretKey key = kgen.generateKey();
System.out.println(new String(key.getEncoded()));
	//	secretKey = new String(key.getEncoded());
//		IV = secretKey.substring(0,16);

		return key.getEncoded();

	}
}


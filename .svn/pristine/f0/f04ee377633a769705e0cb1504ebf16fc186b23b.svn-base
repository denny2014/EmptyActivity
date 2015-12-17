package com.zet.utils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/**
 * AES加密工具
 * 
 * @date 2015年9月14日 下午8:19:00
 * @author 杨学峰
 */
public class AESCryptUtil {

	private static String TYPE = "AES";

	private static int KeySizeAES128 = 16; 

	private static int BUFFER_SIZE = 8192;         

	private static Cipher getCipher(int mode,String key) {
		//mode =Cipher.DECRYPT_MODE or Cipher.ENCRYPT_MODE
		Cipher mCipher;
		byte[]keyPtr=new byte[KeySizeAES128];
		IvParameterSpec ivParam=new IvParameterSpec(keyPtr);
		byte[]passPtr=key.getBytes();

		try{
			mCipher=Cipher.getInstance(TYPE+"/CBC/PKCS5Padding");
			for(int i=0;i<KeySizeAES128;i++) {
				if(i<passPtr.length){
					keyPtr[i]=passPtr[i];
				}else{
					keyPtr[i]=0;
				}
			}

			SecretKeySpec keySpec=new SecretKeySpec(keyPtr,TYPE);
			mCipher.init(mode, keySpec, ivParam);

			return mCipher;
		}catch (InvalidKeyException e) {
			e.printStackTrace();
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * 解密文件
	 * 
	 * @param srcFile 加密的文件
	 * @param destFile 解密后的文件
	 * @param privateKey 密钥
	 */
	public static boolean decrypt(String srcFile, String destFile, String privateKey) {   
		byte[] readBuffer=new byte[BUFFER_SIZE];
		Cipher deCipher=getCipher(Cipher.DECRYPT_MODE,privateKey);
		if(deCipher==null){
			return false; //init failed.
		}
		CipherInputStream fis=null;
		BufferedOutputStream fos=null; 
		int size;
		try {   
			fis = new CipherInputStream(new BufferedInputStream(new FileInputStream(srcFile)),deCipher);   
			fos = new BufferedOutputStream(new FileOutputStream(mkdirFiles(destFile)));   
			while((size=fis.read(readBuffer,0,BUFFER_SIZE))>=0) {
				fos.write(readBuffer,0,size);
			}
			fos.flush();
		} catch (FileNotFoundException e) {   
			e.printStackTrace();   
			return false;
		} catch (IOException e) {   
			e.printStackTrace();   
			return false;
		} finally {   
			if (fis != null) {
				try { 
					fis.close(); 
				} catch(IOException e) {}   
			}   
			if (fos != null) {
				try {
					fos.flush(); 
				} catch(IOException e) {}
				try { 
					fos.close(); 
				} catch(IOException e) {}
			}   
		}   
		return true;
	}


	/**
	 * 加密文件
	 * 
	 * @param srcFile 源文件
	 * @param destFile 加密文件
	 * @param privateKey 密钥
	 * @return true成功，false失败
	 */
	public static boolean encrypt(String srcFile, String destFile, String privateKey) {
		byte[] readBuffer=new byte[BUFFER_SIZE];
		Cipher enCipher=getCipher(Cipher.ENCRYPT_MODE,privateKey);
		if(enCipher==null){
			return false; //init failed.
		}

		CipherOutputStream fos=null;
		BufferedInputStream fis=null;
		int size;

		try {
			fos = new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(destFile)),enCipher);   
			fis = new BufferedInputStream(new FileInputStream(mkdirFiles(srcFile)));   
			while((size=fis.read(readBuffer,0,BUFFER_SIZE))>=0) {
				fos.write(readBuffer,0,size);
			}
			fos.flush();
		} catch (FileNotFoundException e) {   
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();   
			return false;
		} finally {
			if (fis != null) {
				try { 
					fis.close(); 
				} catch(IOException e) {}   

			}   

			if (fos != null) {

				try { 
					fos.flush(); 
				} catch(IOException e) {}

				try { 
					fos.close(); 
				} catch(IOException e) {}

			}   
		} 
		
		return true;
	}

	private static File mkdirFiles(String filePath) throws IOException {   
		File file = new File(filePath);   
		if (!file.getParentFile().exists()) {   
			file.getParentFile().mkdirs();
		}   
		file.createNewFile();
		return file;
	}

	public static void main(String[] args) {
//		AESCrpytUtil.crypt("T:/2.docx", "T:/2-crypt.docx", "123456");
//		AESCrpytUtil.decrypt("T:/2-crypt.docx", "T:/2-crypt-d.docx", "123456");
		String a = "T:/1/2.docx";
		File f = new File(a);
		
		System.out.println(f.getName());
	}

}

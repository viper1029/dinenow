package com.dinenowinc.dinenow.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Hash {
	
	public static String md5Java(String message){
		String digest = null;
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(message.getBytes("UTF-8"));
			
			//converting byte array to Hexadecimal String 
			StringBuilder sb = new StringBuilder(2*hash.length);
			for(byte b : hash){
				sb.append(String.format("%02x", b&0xff));
			}
			
			digest = sb.toString();

		}catch(UnsupportedEncodingException ex){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, null, ex);
		}catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, null, ex);
		}
		return digest;
	}
	
	/* 
	 * Spring framework also provides overloaded md5 methods. You can pass input 
	 * as String or byte array and Spring can return hash or digest either as byte 
	 * array or Hex String. Here we are passing String as input and getting 
	 * MD5 hash as hex String. 
	 */
	public static String md5Spring(String text){
		return DigestUtils.md5Hex(text);
	}
	
	/* 
	 * Apache commons code provides many overloaded methods to generate md5 hash. It contains 
	 * md5 method which can accept String, byte[] or InputStream and can return hash as 16 element byte 
	 * array or 32 character hex String. 
	 */
	public static String md5ApacheCommonsCodec(String content){
		return DigestUtils.md5Hex(content);
	}
	
//	Get Random Hexadecimal String
	public static String getRandomHexString(){
	        Random r = new Random();
	        StringBuffer sb = new StringBuffer();
	        while(sb.length() < 6){
	            sb.append(Integer.toHexString(r.nextInt()));
	        }

	        return sb.toString().substring(0, 6);
	    }

}

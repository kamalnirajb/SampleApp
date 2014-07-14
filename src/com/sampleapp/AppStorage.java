package com.sampleapp;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.SharedPreferences;


public class AppStorage {
	
	public static SharedPreferences getAppSharedPreferencesForRead()
	{
		try{
				String appSharedPreferencesFile = AppActivity.this_package_name;
				return
				AppActivity.this_app.getSharedPreferences(
						appSharedPreferencesFile, 
						AppActivity.MODE_PRIVATE);
		} catch (Exception e) {
			AppLog.d(e.toString());
		}
		return null;
	}
	public static SharedPreferences.Editor getAppSharedPreferencesForWrite()
	{
		try{
				String appSharedPreferencesFile = AppActivity.this_package_name;
				return
				AppActivity.this_app.getSharedPreferences(
						appSharedPreferencesFile, 
						AppActivity.MODE_PRIVATE).edit();
		} catch (Exception e) {
			AppLog.d(e.toString());
		}
		return null;
	}
	
	public static void writeAppInternalStorageFileAsString(String fileName,String data)
	{
		try{
			//writeAppInternalStorageFileAsByte(fileName,data.getBytes("UTF-8"));
			writeAppInternalStorageFileAsByte(fileName,stringToBytesUTFCustom(data));			
		}catch(Exception e){
			AppLog.d(e.toString());
		}
	}
	
	public static void writeAppInternalStorageFileAsByte(String fileName,byte [] byteData)
	{
		FileOutputStream fos = null;
		
		//AppLog.d("total byte write "+byteData.length);
		
		try{
			fos = AppActivity.this_app.openFileOutput(fileName, Context.MODE_PRIVATE);
		}catch(Exception e){
			AppLog.d(e.toString());
		}
		
		if(fos == null)
			return;
		
		try{
			fos.write(byteData);
			fos.close();
			
			return;
		} catch(Exception e) {
			AppLog.d(e.toString());
		}
	}
	
	public static String readAppInternalStorageFileAsString(String fileName)
	{
		byte[] b = readAppInternalStorageFileAsByte(fileName);
		
		if(b == null)
			return null;
		
		String str = null;
		try{
			//str = new String(b,"UTF-8");
			str = bytesToStringUTFCustom(b);
		}catch(Exception e){
			
		}
		return str;
	}
	
	public static byte[] readAppInternalStorageFileAsByte(String fileName)
	{
		FileInputStream fis = null;
		try{
			fis = AppActivity.this_app.openFileInput(fileName);
		}catch(Exception e){
			AppLog.d(e.toString());
		}
		
		if(fis == null)
			return null;
		
		try{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			int fileChunkSize = 1024 * 65;
			byte[] bytes = new byte[fileChunkSize];
            int size;
            
            while ((size = fis.read(bytes,0,fileChunkSize)) !=-1) {
            	bos.write(bytes, 0, size);
            }
			fis.close();
			byte [] byte_arr= bos.toByteArray();
			bos.close();
			//AppLog.d("total byte read "+byte_arr.length);
			return byte_arr;
		} catch(Exception e) {
			AppLog.d(e.toString());
		}
		return null;
	}
	
	public static String bytesToStringUTFCustom(byte[] bytes) {
		 char[] buffer = new char[bytes.length >> 1];
		 for(int i = 0; i < buffer.length; i++) {
		  int bpos = i << 1;
		  char c = (char)(((bytes[bpos]&0x00FF)<<8) + (bytes[bpos+1]&0x00FF));
		  buffer[i] = c;
		 }
		 return new String(buffer);
	}
	public static byte[] stringToBytesUTFCustom(String str) {
		 char[] buffer = str.toCharArray();
		 byte[] b = new byte[buffer.length << 1];
		 for(int i = 0; i < buffer.length; i++) {
		  int bpos = i << 1;
		  b[bpos] = (byte) ((buffer[i]&0xFF00)>>8);
		  b[bpos + 1] = (byte) (buffer[i]&0x00FF);
		 }
		 return b;
	}
}

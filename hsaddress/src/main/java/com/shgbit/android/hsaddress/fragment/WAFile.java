package com.shgbit.android.hsaddress.fragment;

import android.util.Log;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class WAFile {
	
	private static final String WAFile_TAG = "WarfLib:WAFile";
	
	private static void CheckFile(String filename) {
		
		try {
			File file = new File(filename);
			
			if (file.getParentFile().exists() == false) {
				file.getParentFile().mkdirs();
		}
		
		} catch (Exception e) {
			Log.e(WAFile_TAG, "Exception:" + e.toString());
			return;
		}
	}


	public static boolean write(String filename, boolean append, String str) {
		
		CheckFile(filename);
		
		FileWriter fw;
		try {
			fw = new FileWriter(filename, append);
			
			fw.write(str);
			
			fw.close();
			return true;
		} catch (IOException e) {
			Log.e(WAFile_TAG, "Exception:" + e.toString());
			return false;
		}
	}
	

	public static boolean write(String filename, boolean append, char[] arrc) {
		
		CheckFile(filename);
		
		FileWriter fw;
		try {
			fw = new FileWriter(filename, append);
			
			fw.write(arrc);
			
			fw.close();
			return true;
		} catch (IOException e) {
			Log.e(WAFile_TAG, "Exception:" + e.toString());
			return false;
		}
	}
	

	public static boolean write(String filename, boolean append, byte[] arrb) {
		
		CheckFile(filename);
		
		FileOutputStream f = null;
        try {
            File file = new File(filename);
            if (file.getParentFile().exists() == false) {
                file.getParentFile().mkdir();
            }
            f = new FileOutputStream(filename, append);
            f.write(arrb);
            f.close();
        } catch (Exception e) {
        	Log.e(WAFile_TAG, "Exception:" + e.toString());
            return false;
        }
        return true;
	}


	public static byte[] readArrayByte(String filename) {
		
        byte[] data = null;

        try {
        	
        	if (new File(filename).exists() == false) {
        		Log.e(WAFile_TAG, "readArrayByte file:" + filename + "is not exist!");
        		return null;
        	}
        	
            FileInputStream fs = new FileInputStream(filename);
            data = new byte[fs.available()];
            fs.read(data);
            fs.close();
            return data;
        } catch(Exception e) {
            Log.e(WAFile_TAG, "Exception:" + e.toString());
            return null;
        }
	}
	

	public static String readString(String filename) {
		
		try{
			
			String str = "";
			
			if (new File(filename).exists() == false) {
				Log.e(WAFile_TAG, "readString file:" + filename + "is not exist!");
        		return null;
        	}

			FileInputStream fs = new FileInputStream(filename);
		    int length = fs.available(); 
		    byte [] buffer = new byte[length]; 
		    fs.read(buffer);     
		    str = EncodingUtils.getString(buffer, "UTF-8");
		    fs.close();
		    
		    return str;

		} catch(Exception e) {
	    	Log.e(WAFile_TAG, "Exception:" + e.toString());
	    	return null;
		}
	}
	
	public static String[] listAllFile(File file) {
		
		ArrayList<String> fs = new ArrayList<String>();
		
		listFile(file, fs);
		
		return fs.toArray(new String[0]);
	}
	
	private static void listFile(File file, ArrayList<String> fs) {
		
		File [] fl = file.listFiles();
		for (int i = 0; i < fl.length; i++) {
			if (fl[i].isDirectory()) {
				listFile(fl[i], fs);
			} else if (fl[i].isFile()) {
				fs.add(fl[i].getPath());
			}
		}
	}
	
	public static void deleteFile(File file, boolean delDir) {

		try {
			
			if (file.exists()) {
				if (file.isFile()) {
					file.delete();
				} else if (file.isDirectory()) {
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						deleteFile(files[i], delDir);
					}
				}
				if (delDir == true) {
					file.delete();
				}
			}
		
		} catch (Throwable e) {
			Log.e(WAFile_TAG, "deleteFile Throwable:" + e.toString());
		}
	}
}

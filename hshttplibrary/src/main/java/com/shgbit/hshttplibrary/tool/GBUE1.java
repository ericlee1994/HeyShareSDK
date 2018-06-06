package com.shgbit.hshttplibrary.tool;

import android.util.Base64;

public class GBUE1 {

	public static String encode(String data, String key) {
		String result = "";
		
		try {
		
			byte [] bData = data.getBytes("UTF-8");
			byte [] bKey = key.getBytes("UTF-8");
			
			for (int i = 0; i < bData.length; i++) {
				
				byte m = bData[i];
				
				for (int j = 0; j < bKey.length; j++) {
					
					m ^= bKey[j];
				}
				
				bData[i] = m;
				
			}
			result = Base64.encodeToString(bData, Base64.DEFAULT);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}

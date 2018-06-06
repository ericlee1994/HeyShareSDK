package com.shgbit.hshttplibrary.tool;



import com.shgbit.hshttplibrary.json.Meeting;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;


public class ComparatorTime implements Comparator<Meeting> {

	@Override
	public int compare(Meeting arg0, Meeting arg1) {
		if (arg0 == null || arg0 == null) {
			return 0;
		}
		
		long d1 = getTimeMillis(arg0.getStartTime());
		long d2 = getTimeMillis(arg1.getStartTime());
		
		if (d1 > d2) {
			return 1;
		} else if (d1 < d2) {
			return -1;
		}
		return 0;
	}

	public long getTimeMillis(String timeStr) {
		SimpleDateFormat sDateSDF;
		if (timeStr.contains("/") == true){
			sDateSDF = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA);
		}else {
			sDateSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
		}
		long l = 0;
		Date d;
		try {
			d = sDateSDF.parse(timeStr);
			l = d.getTime();

		} catch (Exception e) {

		}
		return l;
	}

}

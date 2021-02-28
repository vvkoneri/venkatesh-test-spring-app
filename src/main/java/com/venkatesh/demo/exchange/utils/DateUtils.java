package com.venkatesh.demo.exchange.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
	
	public static String getDateInFormatAndTimeZone(String format, String timeZone) {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat(format);
		
		df.setTimeZone(TimeZone.getTimeZone(timeZone));
		
		return df.format(date);
	}
	
	public static String getPreviousDateInFormatAndTimeZone(String format, String timeZone) {
		Instant now = Instant.now();
		Instant yesterday = now.minus(1, ChronoUnit.DAYS);
		DateFormat df = new SimpleDateFormat(format);
		
		df.setTimeZone(TimeZone.getTimeZone(timeZone));
		
		return df.format(Date.from(yesterday));
	}
}

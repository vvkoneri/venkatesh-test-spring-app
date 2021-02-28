package com.venkatesh.demo.constant;

public class Constant {
	public static final String ECB_URL = "ecb.exchange.rate.reference.url";
	public static final String ECB_DATA_REFRESH_TIME = "ecb.exchange.rate.reference.refresh.cron";
	public static final String ECB_DATA_TIMEZONE = "ecb.exchange.rate.reference.refresh.timezone";
	
	public final static String ECB_CRON_TIME = "0 01 16 * * *";
	public final static String ECB_TIME_ZONE = "CET";
	
	public static boolean condition = false;
	
	public static String publicLink = "https://finance.yahoo.com/quote/%s=X/";
	
	public static final String LINK_RESPONSE = "{ \"link\" : \"%s\" }";
	public static final String CONVERT_CURRENCY_AMOUNT_RESPONSE = "{ \"Result\" : \"%s %s = %s %s\"}";
	public static final String REQUEST_STATS_RESPONSE = "{ \"RequestStats\" : { %s } }";
	public static final String SUPPORTED_CURRENCY_RESPONSE = "{ \"Currencies\" : %s }";
	public static final String CONVERT_CURRENCY_RESPONSE = "{ \"1 %s\" : \"%.2f %s \"}";
	public static final String ECB_EUR_REFERENCE_RATE_RESPONSE = "{ \"Currency\" : \"%s\" , \"Rate\" : %s }";
}

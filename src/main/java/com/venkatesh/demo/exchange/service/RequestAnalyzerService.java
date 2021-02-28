package com.venkatesh.demo.exchange.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestAnalyzerService {
	
	private ExchangeRateService exchangeRateService;
	
	private Map<String,Long> currencyRequests = new HashMap<String,Long>();
	
	@Autowired
	public RequestAnalyzerService(ExchangeRateService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;
	}
	
	@PostConstruct
	public void loadCurrencies() {
		Set<String> supportedCurrencies = exchangeRateService.getSupportedCurrencies();
		
		for(String curr : supportedCurrencies) {
			this.currencyRequests.put(curr, 0L);
		}
	}
	
	public void incrementRequest(String curr) {
		this.currencyRequests.put(curr, this.currencyRequests.get(curr) + 1);
	}
	
	public Map<String,Long> getRequestStats() {
		return this.currencyRequests;
	}

}

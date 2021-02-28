package com.venkatesh.demo.exchange.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.venkatesh.demo.constant.Constant;
import com.venkatesh.demo.exchange.service.ExchangeRateService;


@Configuration
@EnableScheduling
public class ECBReferenceRateJob {
	
	@Autowired
	private ExchangeRateService exchangeRateService;
	
	@Scheduled(cron = Constant.ECB_CRON_TIME, zone=Constant.ECB_TIME_ZONE)
	public void scheduleExchangeRateLoadJob() {
		try {
			exchangeRateService.loadEcbReferenceRates();
		} catch (Exception e) {
			System.out.println("Unable to fetch exchange rates : " + e.getMessage());
		}
	}

}

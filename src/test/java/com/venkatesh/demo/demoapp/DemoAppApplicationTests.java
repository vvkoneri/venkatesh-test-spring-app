package com.venkatesh.demo.demoapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.venkatesh.demo.exchange.service.ExchangeRateService;

@SpringBootTest
class DemoAppApplicationTests {
	
	@Autowired
	private ExchangeRateService service;

	@Test
	void contextLoads() {
		try {
			service.loadEcbReferenceRates();
			assertThat(service.getReferenceRates().size() > 0);
		} catch (Exception e) {
			System.out.println("Error message while testing : " + e.getMessage());
		}
		
	}

}

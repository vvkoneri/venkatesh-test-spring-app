package com.venkatesh.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.venkatesh.demo.constant.Constant;
import com.venkatesh.demo.controller.exceptions.InvalidInputException;
import com.venkatesh.demo.exchange.service.ExchangeRateService;
import com.venkatesh.demo.exchange.service.RequestAnalyzerService;

@RestController
public class ExchangeRateWebService {
	
	Logger logger = LoggerFactory.getLogger(ExchangeRateWebService.class);

	private ExchangeRateService exchangeRateSvc;
	private RequestAnalyzerService requestAnalyzer;

	@Autowired
	public ExchangeRateWebService(ExchangeRateService exchangeRateSvc, RequestAnalyzerService requestAnalyzer) {
		this.exchangeRateSvc = exchangeRateSvc;
		this.requestAnalyzer = requestAnalyzer;
	}

	@RequestMapping(value = "/rate/eur", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getECBEURReferenceRate(@RequestParam(value = "currency") String currency)
			throws InvalidInputException {
		Double rate = exchangeRateSvc.getRateForCurrency(currency);
		if (rate == null) {
			logger.error("Unsupopred currency format " + currency);
			throw new InvalidInputException("No valid curreny information provided");
		}
		String response = String.format(Constant.ECB_EUR_REFERENCE_RATE_RESPONSE, currency, rate);
		requestAnalyzer.incrementRequest(currency);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/currencies/request-stats", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getRequestStats() {
		Map<String,Long> stats = requestAnalyzer.getRequestStats();
		List<String> statsStr = new ArrayList<String>();
		
		for(Map.Entry<String, Long> mapEntry : stats.entrySet()) {
			statsStr.add("\"" + mapEntry.getKey() + "\"" + ":" + String.valueOf(mapEntry.getValue()));
		}
		
		String response = String.format(Constant.REQUEST_STATS_RESPONSE, statsStr.stream().map(Object::toString)
                .collect(Collectors.joining(", ")));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/currencies/supported", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getSupportedCurrencies() {
		Set<String> currencies = exchangeRateSvc.getSupportedCurrencies();
		
		String[] currArr = new String[currencies.size()];
		int i = 0;
		for(String curr : currencies) {
			currArr[i++] = curr;
		}
		String response = String.format(Constant.SUPPORTED_CURRENCY_RESPONSE, Arrays.toString(currArr));
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/convert", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getExchangeRate(@RequestParam(value = "from") String from,
			@RequestParam(value = "to") String to) throws InvalidInputException {
		Double rate = exchangeRateSvc.convertCurrency(from, to);

		String response = String.format(Constant.CONVERT_CURRENCY_RESPONSE, from, rate, to);
		logger.info("Request stats updated for " + from + " and " + to );
		requestAnalyzer.incrementRequest(from);
		requestAnalyzer.incrementRequest(to);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/convert/amount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getCurrencyConversion(@RequestParam(value = "from") String from,
			@RequestParam(value = "to") String to, @RequestParam(value = "amount") String amount) throws InvalidInputException {
		Double rate = exchangeRateSvc.convertCurrency(from, to);
		Double rateWithAmt = rate * Integer.parseInt(amount);

		String response = String.format(Constant.CONVERT_CURRENCY_AMOUNT_RESPONSE, amount, from, Math.round(rateWithAmt * 100.0) / 100.0, to);
		requestAnalyzer.incrementRequest(from);
		requestAnalyzer.incrementRequest(to);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/chart", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getChartLink(@RequestParam(value = "currency1") String currency1,
			@RequestParam(value = "currency2") String currency2) throws InvalidInputException {
		
		String link = String.format(Constant.publicLink, currency1.toUpperCase() + "" + currency2.toUpperCase());
		return new ResponseEntity<>(String.format(Constant.LINK_RESPONSE, link), HttpStatus.OK);
	}
	
}

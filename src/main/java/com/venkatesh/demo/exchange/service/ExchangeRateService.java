package com.venkatesh.demo.exchange.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.venkatesh.demo.controller.exceptions.InvalidInputException;
import com.venkatesh.demo.exchange.client.EcbClient;

@Service
public class ExchangeRateService {

	private EcbClient ecbClient;

	@Autowired
	public ExchangeRateService(EcbClient ecbClient) {
		this.ecbClient = ecbClient;
	}

	private HashMap<String, Double> referenceRates = new HashMap<String, Double>();

	@PostConstruct
	public void loadEcbReferenceRates() throws Exception {
		Document xmlDocResponse = ecbClient.getExchangeRateAsXml();
		NodeList exchangeList = xmlDocResponse.getElementsByTagName("Cube");
		for (int idx = 0; idx < exchangeList.getLength(); idx++) {
			Node node = exchangeList.item(idx);
			if (node.getNodeType() == Element.ELEMENT_NODE) {
				Element eElement = (Element) node;

				if (eElement.getAttribute("time") != null
						&& !eElement.getAttribute("time").isEmpty()) {
					NodeList rates = eElement.getChildNodes();
					for (int c_idx = 0; c_idx < rates.getLength(); c_idx++) {
						Node c_node = rates.item(c_idx);
						if (c_node.getNodeType() == Element.ELEMENT_NODE) {
							Element c_eElement = (Element) c_node;
							referenceRates.put(c_eElement.getAttribute("currency"),
									Double.valueOf(c_eElement.getAttribute("rate")));
						}
					}
				}
			}
		}
	}

	public HashMap<String, Double> getReferenceRates() {
		return this.referenceRates;
	}

	public Double convertCurrency(String from, String to) throws InvalidInputException {
		Double fromCur = this.referenceRates.get(from);
		Double toCur = this.referenceRates.get(to);

		if (fromCur == null || toCur == null) {
			throw new InvalidInputException("Unsupported Curreny format specified");
		}

		Double convertedCur = toCur / fromCur;

		return convertedCur;
	}

	public Double getRateForCurrency(String currency) {
		return this.referenceRates.get(currency);
	}

	public Set<String> getSupportedCurrencies() {
		Set<String> supportedCurrencies = new HashSet<String>();
		for (Map.Entry<String, Double> rate : referenceRates.entrySet()) {
			supportedCurrencies.add(rate.getKey());
		}
		return supportedCurrencies;
	}

	public void printRates() {
		for (Map.Entry<String, Double> rate : referenceRates.entrySet()) {
			System.out.println("Currency : " + rate.getKey() + "------" + "Value : " + rate.getValue());
		}
	}

}

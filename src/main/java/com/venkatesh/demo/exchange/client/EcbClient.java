package com.venkatesh.demo.exchange.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

@Service
public class EcbClient {
	
	final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    
	
	@Value("${ecb.exchange.rate.reference.url}")
	private String ecbUrl;
	
	private HttpClient httpClient;
	
	@Autowired
	public EcbClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public Document getExchangeRateAsXml() throws Exception {
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		byte[] responseInBytes = httpClient.makeGetRequest(ecbUrl,new HashMap<String, String>() {{
	        put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
	    }}).bodyToMono(byte[].class).block();
		
		InputStream myInputStream = new ByteArrayInputStream(responseInBytes);
		return builder.parse(myInputStream);
	}

}

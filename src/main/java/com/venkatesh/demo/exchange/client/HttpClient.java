package com.venkatesh.demo.exchange.client;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;


@Service
public class HttpClient {

	private WebClient client = WebClient.create();
	
	public ClientResponse makeGetRequest(String url, Map<String,String> headers) {
		RequestHeadersUriSpec<?> request = client.get();
		for(Map.Entry<String,String> entry : headers.entrySet()) {
			request.header(entry.getKey(),entry.getValue());
		}
		return request.uri(url).exchange().block();
	}
}

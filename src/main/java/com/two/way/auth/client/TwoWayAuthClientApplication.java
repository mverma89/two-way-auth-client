package com.two.way.auth.client;

import java.security.KeyStore;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class TwoWayAuthClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwoWayAuthClientApplication.class, args);
	}
	
	
	@Bean
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		
		KeyStore keyStore;
		HttpComponentsClientHttpRequestFactory requestFactory = null;
		
		try {
			keyStore = KeyStore.getInstance("jks");
			ClassPathResource classPathResource = new ClassPathResource("atlas-client.jks");
			keyStore.load(classPathResource.getInputStream(), "Invwitron@2020".toCharArray());
			
			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(new SSLContextBuilder()
					.loadTrustMaterial(null, new TrustSelfSignedStrategy())
					.loadKeyMaterial(keyStore, "Invwitron@2020".toCharArray())
					/*
					 * .loadKeyMaterial(keyStore, "Invwitron@2020".toCharArray(), new
					 * PrivateKeyStrategy() {
					 * 
					 * @Override public String chooseAlias(Map<String, PrivateKeyDetails> aliases,
					 * Socket socket) { // TODO Auto-generated method stub return
					 * "inventory-server-witron"; } })
					 */
					.build(),
					NoopHostnameVerifier.INSTANCE);
			
			HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
					.setMaxConnTotal(Integer.valueOf(5))
					.setMaxConnPerRoute(Integer.valueOf(5))
					.build();
			
			requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			requestFactory.setReadTimeout(Integer.valueOf(10000));
			requestFactory.setConnectTimeout(Integer.valueOf(10000));
			
			restTemplate.setRequestFactory(requestFactory);
		}
		catch (Exception e) {
			System.out.println("Error creating ssl connection resttemplate "+e.getMessage());
			e.printStackTrace();
		}
		
		return restTemplate;
	}

}

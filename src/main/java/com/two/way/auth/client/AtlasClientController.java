package com.two.way.auth.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("atlas/client")
public class AtlasClientController {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	Environment env;
	
	@GetMapping("/snap")
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public String getAtlasSnap() {
		return "Returning atlas snap!!";
	}
	
	@GetMapping("/wsnap")
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public String getAtlasWitSnap() {
		
		String witronEndpoint = env.getProperty("endpoint.witron.server.snap");
		System.out.println("Calling https two way auth witron server endpoint!! "+witronEndpoint);
		return restTemplate.getForObject(witronEndpoint, String.class);
	}

}

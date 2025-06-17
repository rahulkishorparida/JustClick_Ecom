package com.jc.demo.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jc.demo.model.FormResponse;
import com.jc.demo.service.FormService;
@Service
public class FormServiceImpl  implements FormService{
	@Value("${form.api.key}")
	private String apikey;
	
 

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<FormResponse> getAllForms() {
        String url = apikey + "?apikey=" + apikey;
        FormResponse[] responseArray = restTemplate.getForObject(url, FormResponse[].class);
        return Arrays.asList(responseArray);
    }

}

package com.kalaerik.productmng.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalaerik.productmng.model.Rate;
import com.kalaerik.productmng.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CurrencyClient {

    @Value("${conversion.resourceUrl}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    public double getHrkEurRate() throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        Rate rate = new Rate(
                //mapper.readTree(response.getBody()).get(0).get(Constants.RATE_NUMBER).asText(),
                //mapper.readTree(response.getBody()).get(0).get(Constants.CHANGE_DATE).asText(),
                //mapper.readTree(response.getBody()).get(0).get(Constants.STATE).asText(),
               // mapper.readTree(response.getBody()).get(0).get(Constants.CURRENCY_CODE).asText(),
                mapper.readTree(response.getBody()).get(0).get(Constants.CURRENCY).asText(),
                //mapper.readTree(response.getBody()).get(0).get(Constants.UNIT).asInt(),
                mapper.readTree(response.getBody()).get(0).get(Constants.BUYING_RATE).asText(),
                mapper.readTree(response.getBody()).get(0).get(Constants.MIDDLE_RATE).asText(),
                mapper.readTree(response.getBody()).get(0).get(Constants.SELLING_RATE).asText()
        );
        return Double.parseDouble(rate.getMiddleRate().replaceAll(",","\\."));
    }
/*
    public double convertHrkToEur(double hrkPrice, double rate){
        return Math.round((hrkPrice / rate) * 100.0) / 100.0;
    }
*/

}

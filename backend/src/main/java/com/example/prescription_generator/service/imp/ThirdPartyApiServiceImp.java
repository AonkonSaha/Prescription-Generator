package com.example.prescription_generator.service.imp;

import com.example.prescription_generator.model.dto.CovidCountryStats;
import com.example.prescription_generator.service.ThirdPartyApiService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ThirdPartyApiServiceImp implements ThirdPartyApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<CovidCountryStats> getCovidCountryStats() {
            String url = "https://disease.sh/v3/covid-19/countries";

            ResponseEntity<List<CovidCountryStats>> response = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            return response.getBody();
        }

}

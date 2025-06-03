package com.example.prescription_generator.service;

import com.example.prescription_generator.model.dto.CovidCountryStats;

import java.util.List;

public interface ThirdPartyApiService {
    List<CovidCountryStats> getCovidCountryStats();
}

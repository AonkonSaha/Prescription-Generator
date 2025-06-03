package com.example.prescription_generator.model.dto;


import lombok.Data;

@Data
public class CovidCountryStats {

    private String country;
    private long cases;
    private long todayCases;
    private long deaths;
    private long todayDeaths;
    private long recovered;
    private String flag;
}

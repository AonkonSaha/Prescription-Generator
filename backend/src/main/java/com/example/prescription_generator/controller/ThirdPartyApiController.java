package com.example.prescription_generator.controller;

import com.example.prescription_generator.model.dto.CovidCountryStats;
import com.example.prescription_generator.service.ThirdPartyApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/third-party")
@RequiredArgsConstructor
public class ThirdPartyApiController {

    private final ThirdPartyApiService thirdPartyApiService;

    @GetMapping("/covid-stats")
    public ResponseEntity<List<CovidCountryStats>> getCovidStats() {
        return ResponseEntity.ok(thirdPartyApiService.getCovidCountryStats());
    }


}

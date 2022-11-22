package com.example.takehome.controller;

import com.example.takehome.model.dto.CountryCharadeApi;
import com.example.takehome.service.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping
public class CountryCharadeController {

    private final CountryService countryService;

    public CountryCharadeController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/country-charade")
    private Mono<CountryCharadeApi> countries(@RequestParam("codes") List<String> countryCodes) {
        return countryService.solveCharade(countryCodes);
    }
}
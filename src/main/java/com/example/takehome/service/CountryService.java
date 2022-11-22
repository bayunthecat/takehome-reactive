package com.example.takehome.service;

import com.example.takehome.client.CountriesServiceClient;
import com.example.takehome.client.dto.ContinentDto;
import com.example.takehome.client.dto.CountryDto;
import com.example.takehome.model.dto.ContinentApi;
import com.example.takehome.model.dto.CountryCharadeApi;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final CountriesServiceClient countriesServiceClient;

    public CountryService(CountriesServiceClient countriesServiceClient) {
        this.countriesServiceClient = countriesServiceClient;
    }

    //TODO rename method
    //TODO rewrite method to be more readable
    public Mono<CountryCharadeApi> solveCharade(List<String> countryCodes) {
        return countriesServiceClient.getCountriesByCodes(countryCodes)
                .map(countries -> countries.stream().collect(Collectors.groupingBy(country -> country.getContinent().getCode(), Collectors.collectingAndThen(Collectors.toList(), groupedCountries -> groupedCountries.stream().map(CountryDto::getCode).toList()))))
                .flatMap(countriesByContinent -> {
                    final var monoList = countriesByContinent.entrySet().stream().map(entry -> {
                                final var continent = entry.getKey();
                                final var countries = entry.getValue();
                                return countriesServiceClient.getCountriesByContinent(List.of(continent), countries)
                                        .map(response -> ContinentApi.builder()
                                                .countries(countries)
                                                .name(response.stream().findAny().map(CountryDto::getContinent).map(ContinentDto::getName).orElse("Unknown"))
                                                .otherCountries(response.stream().map(CountryDto::getCode).toList())
                                                .build());
                            })
                            .toList();
                    return Mono.zip(monoList, args -> {
                        final var continents = Arrays.stream(args)
                                .map(arg -> (ContinentApi) arg)
                                .toList();
                        return CountryCharadeApi.builder()
                                .continents(continents)
                                .build();
                    });
                })
                //TODO remove later, throw exception
                .onErrorReturn(CountryCharadeApi.builder().build());
    }
}

package com.example.takehome.client;

import com.example.takehome.client.dto.CountriesResponseDto;
import com.example.takehome.client.dto.CountryDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountriesServiceClient {

    private static final String FETCH_COUNTRIES_BY_CODES = "{\"operationName\":null,\"variables\":{},\"query\":\"{\\n  countries(filter: {code: {in: [%s]}}) {\\n    code\\n    continent {\\n      code\\n    }\\n  }\\n}\\n\"}";

    private static final String FETCH_COUNTRIES_BY_CONTINENT_WITH_EXCLUDE = "{\"operationName\":null,\"variables\":{},\"query\":\"{\\n  countries(filter: {continent: {in: [%s]}, code: {nin: [%s]}}) {\\n    code\\n  }\\n}\\n\"}";

    private static final String LIST_ELEM_FORMAT = "\\\"%s\\\"";

    private final WebClient webClient;

    public CountriesServiceClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://countries.trevorblades.com/graphql")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<List<CountryDto>> getCountriesByCodes(List<String> codes) {
        final var formattedCode = formatCodes(codes);
        return webClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(String.format(FETCH_COUNTRIES_BY_CODES, formattedCode)), String.class)
                .retrieve()
                .bodyToMono(CountriesResponseDto.class)
                .map(response -> response.getData().getCountries());
    }

    public Mono<List<CountryDto>> getCountriesByContinent(List<String> continentCodes, List<String> excludeCountryCodes) {
        final var formattedContinentCodes = formatCodes(continentCodes);
        final var formattedExcludes = formatCodes(excludeCountryCodes);
        return webClient.post().accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(String.format(FETCH_COUNTRIES_BY_CONTINENT_WITH_EXCLUDE, formattedContinentCodes, formattedExcludes)), String.class)
                .retrieve()
                .bodyToMono(CountriesResponseDto.class)
                .map(response -> response.getData().getCountries());
    }

    private String formatCodes(List<String> codes) {
        return codes.stream().map(code -> String.format(LIST_ELEM_FORMAT, code)).collect(Collectors.joining(","));
    }
}
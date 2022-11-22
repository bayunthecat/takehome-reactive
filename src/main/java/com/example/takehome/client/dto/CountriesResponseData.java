package com.example.takehome.client.dto;

import lombok.Data;

import java.util.List;

@Data
public class CountriesResponseData {

    private List<CountryDto> countries;
}

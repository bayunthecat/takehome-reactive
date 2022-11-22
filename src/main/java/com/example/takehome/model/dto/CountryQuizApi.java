package com.example.takehome.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class CountryQuizApi {

    private List<ContinentApi> continents;

}
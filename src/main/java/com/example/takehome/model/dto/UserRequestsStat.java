package com.example.takehome.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestsStat {

    private volatile String firstRequestAt;

    private volatile int requestsCount;

    //TODO remove later
    private volatile boolean abort;
}


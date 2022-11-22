package com.example.takehome.filter;

import com.example.takehome.service.LimitRequestsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Slf4j
@Component
public class RequestPerSecondLimitFilter implements WebFilter {

    private final LimitRequestsService requestsService;

    public RequestPerSecondLimitFilter(LimitRequestsService requestsService) {
        this.requestsService = requestsService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return exchange.getPrincipal()
                .map(Principal::getName)
                .flatMap(requestsService::checkRequestsExceedPerUser)
                .defaultIfEmpty(exchange.getRequest().getRemoteAddress().getHostString())
                .map(requestsService::checkRequestsExceedPerIp)
                .flatMap(res -> chain.filter(exchange));
    }
}
package com.example.api.service;

import reactor.core.publisher.Mono;

public interface CurrencyService {
    Mono<Double> convertCurrency(double amount, String fromCurrency);

}



package com.example.api.service.impl;

import com.example.api.model.ValCurs;
import com.example.api.model.Valute;
import com.example.api.service.CurrencyService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.io.StringReader;


@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final RestTemplate restTemplate;

    @Value("${external.api.currency.url}")
    String currencyUrl;


    public CurrencyServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Mono<Double> getCurrencyRate(String currencyCode) {

        return Mono.fromCallable(() -> restTemplate.getForObject(currencyUrl, String.class))
                .flatMap(xmlResponse -> {
                    if (xmlResponse == null) {
                        return Mono.error(new RuntimeException("Failed to retrieve currency rates"));
                    }
                    try {
                        JAXBContext jaxbContext = JAXBContext.newInstance(ValCurs.class);
                        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                        ValCurs valCurs = (ValCurs) unmarshaller.unmarshal(new StringReader(xmlResponse));

                        return Mono.just(valCurs.getValutes().stream()
                                .filter(valute -> valute.getCharCode().equalsIgnoreCase(currencyCode))
                                .findFirst()
                                .map(Valute::getDoubleValue)
                                .orElseThrow(() -> new RuntimeException("Currency code not found")));
                    } catch (JAXBException e) {
                        return Mono.error(new RuntimeException("Failed to parse currency XML", e));
                    }
                });
    }

    @Override
    public Mono<Double> convertCurrency(double amount, String fromCurrency) {
        return getCurrencyRate(fromCurrency)
                .map(currencyRate -> {
                    return amount * currencyRate;
                })
                .onErrorMap(e -> new RuntimeException("Error during currency conversion", e));
    }
}

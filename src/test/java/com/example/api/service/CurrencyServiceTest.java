package com.example.api.service;

import com.example.api.service.impl.CurrencyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CurrencyServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrencyRate_Success() {
        String mockResponse = "<ValCurs Date=\"02.03.2024\" name=\"Foreign Currency Market\"><Valute ID=\"R01235\"><NumCode>840</NumCode><CharCode>USD</CharCode><Nominal>1</Nominal><Value>75,00</Value><Value>75.00</Value><Previous>74,50</Previous></Valute></ValCurs>";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);

        Mono<Double> rate = currencyService.getCurrencyRate("USD");

        assertDoesNotThrow(() -> rate.block());
        assertEquals(75.00, rate.block());
    }

    @Test
    void testGetCurrencyRate_InvalidCurrency() {
        String mockResponse = "<ValCurs Date=\"02.03.2024\" name=\"Foreign Currency Market\"><Valute ID=\"R01235\"><NumCode>840</NumCode><CharCode>USD</CharCode><Nominal>1</Nominal><Value>75,00</Value><Previous>74,50</Previous></Valute></ValCurs>";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);

        Mono<Double> rate = currencyService.getCurrencyRate("EUR");

        RuntimeException exception = assertThrows(RuntimeException.class, rate::block);
        assertEquals("Currency code not found", exception.getMessage());
    }

    @Test
    void testConvertCurrency_Success() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("<ValCurs Date=\"02.03.2024\" name=\"Foreign Currency Market\"><Valute ID=\"R01235\"><NumCode>840</NumCode><CharCode>USD</CharCode><Nominal>1</Nominal><Value>75,00</Value><Previous>74,50</Previous></Valute></ValCurs>");

        Mono<Double> result = currencyService.convertCurrency(100, "USD");

        assertEquals(7500.00, result.block());
    }

    @Test
    void testConvertCurrency_Failure() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("<ValCurs Date=\"02.03.2024\" name=\"Foreign Currency Market\"><Valute ID=\"R01235\"><NumCode>840</NumCode><CharCode>USD</CharCode><Nominal>1</Nominal><Value>75,00</Value><Previous>74,50</Previous></Valute></ValCurs>");

        Mono<Double> result = currencyService.convertCurrency(100, "EUR");

        RuntimeException exception = assertThrows(RuntimeException.class, result::block);
        assertEquals("Error during currency conversion", exception.getMessage());
    }
}

package ru.hogwarts.school_test.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
public class InfoController {

    @Value("${server.port}")
    private int serverPort;

    @GetMapping("/port")
    public int getPort() {
        return serverPort;
    }

    @GetMapping("/optimized")
    public long getSumOptimized() {
        long sum = Stream.iterate(1, a -> a + 1)
                .limit(1000000)
                .parallel()
                .mapToLong(Integer::longValue)
                .sum();
        return sum;
    }
    @GetMapping("/optimized2")
    public long getSumUsingMathFormula() {
        // Сумма арифметической прогрессии: S = n * (a1 + an) / 2
        // где n = 1000000, a1 = 1, an = 1000000
        long n = 1000000L;
        return n * (1 + n) / 2;
    }
}

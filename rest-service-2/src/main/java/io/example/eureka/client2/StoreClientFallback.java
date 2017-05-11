package io.example.eureka.client2;

import org.springframework.stereotype.Component;

@Component
public class StoreClientFallback implements StoreClient {

    @Override
    public String get() {
        return "FAILED";
    }
}
package com.capstone.ads.config;

import com.capstone.ads.exception.FeignErrorDecoder;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    @Bean
    public Retryer stableDiffusionRetryer() {
        return new Retryer.Default(100, 1000, 3);
    }
}

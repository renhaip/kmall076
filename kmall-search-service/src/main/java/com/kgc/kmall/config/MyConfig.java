package com.kgc.kmall.config;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shkstart
 * @create 2020-09-18 22:04
 */
@Configuration
public class MyConfig {
    @Value("${spring.es.host}")
    private String  host;

    @Bean
    public JestClient getJestCline() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder(host)
                .multiThreaded(true)
                .build());
        return factory.getObject();
    }
}

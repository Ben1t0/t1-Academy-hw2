package ru.t1academy.java.hw2.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.t1academy.java.hw2.filter.RequestAndResponseFilter;

@Configuration
@ConditionalOnWebApplication
public class WebLoggingAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "web-logging-starter", name = "enabled", havingValue = "true")
    public RequestAndResponseFilter requestResponseFilter() {
        return new RequestAndResponseFilter();
    }

    @Bean
    @ConfigurationProperties("web-logging-starter")
    public LoggerSettings loggerSettings() {
        return new LoggerSettings();
    }
}
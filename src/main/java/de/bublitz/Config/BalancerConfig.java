package de.bublitz.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "master")
@PropertySource("classpath:config.properties")
@Configuration
@Data
public class BalancerConfig {
    private String ip;
    private int port;
}

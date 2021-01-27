package de.bublitz.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "serial")
@PropertySource("classpath:config.properties")
@Data
public class SerialConfig {
    private String port;
}

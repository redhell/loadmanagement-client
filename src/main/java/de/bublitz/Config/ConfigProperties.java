package de.bublitz.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "chargebox")
@Component
@Data
public class ConfigProperties {
    private String name;
    private String evseid;
}

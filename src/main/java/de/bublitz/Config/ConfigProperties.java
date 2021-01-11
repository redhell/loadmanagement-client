package de.bublitz.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "chargebox")
@PropertySource("classpath:config.properties")
@Component
@Data
public class ConfigProperties {
    private String name;
    private String evseid;
    private String starturl;
    private String stopurl;
}

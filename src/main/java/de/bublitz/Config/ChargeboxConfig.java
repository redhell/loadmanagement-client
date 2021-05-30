package de.bublitz.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "chargebox")
@PropertySource("classpath:config.properties")
@Configuration
@Data
public class ChargeboxConfig {
    private String name;
    private String evseid;
    private String starturl;
    private String stopurl;
    private String tag;
}

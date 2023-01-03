package cz.inqool.dl4dh.krameriusplus.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "system.kramerius")
public class KrameriusProperties {

    private String code;

    private String defaultUrl;

    private String registerUrl;
}

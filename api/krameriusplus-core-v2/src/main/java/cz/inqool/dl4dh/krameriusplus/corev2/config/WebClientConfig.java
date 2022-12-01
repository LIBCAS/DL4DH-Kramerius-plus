package cz.inqool.dl4dh.krameriusplus.corev2.config;

import cz.inqool.dl4dh.krameriusplus.core.config.KrameriusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class WebClientConfig {

    public static final String KRAMERIUS_WEB_CLIENT = "krameriusWebClient";

    private String krameriusUrl;

    @Bean(KRAMERIUS_WEB_CLIENT)
    public WebClient webClient() {
        return WebClient.create(
                UriComponentsBuilder.fromUriString(krameriusUrl)
                        .path("/search/api/v5.0/item")
                        .build()
                        .toUriString());
    }

    @Autowired
    public void setKrameriusUrl(KrameriusInfo krameriusInfo) {
        this.krameriusUrl = (String) krameriusInfo.getInfo().get("url");
    }
}

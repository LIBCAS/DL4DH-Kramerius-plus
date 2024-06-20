package cz.inqool.dl4dh.krameriusplus.core.kramerius;

import cz.inqool.dl4dh.krameriusplus.api.KrameriusInfo;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.krameriusplus.core.kramerius.v5.KrameriusV5Messenger;
import cz.inqool.dl4dh.krameriusplus.core.kramerius.v7.KrameriusV7Messenger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@Slf4j
public class MessengerConfiguration {

    private final String krameriusUrl;

    private final String krameriusVersion;

    private static final int MAX_MEMORY_SIZE = 16777216;

    @Autowired
    public MessengerConfiguration(KrameriusInfo krameriusInfo) {
        krameriusUrl = (String) krameriusInfo.getInfo().get("url");
        krameriusVersion = (String) krameriusInfo.getInfo().get("version");
    }

    @Bean
    public KrameriusMessenger krameriusMessenger(DigitalObjectMapperVisitor mapper) {
        if (krameriusVersion.startsWith("7")) {
            log.info("Initializing v7 version of messenger.");
            return new KrameriusV7Messenger(v7Client(), mapper);
        } else if (krameriusVersion.startsWith("5")) {
            log.info("Initializing v5 version of messenger.");
            return new KrameriusV5Messenger(v5Client(), mapper);
        }

        throw new IllegalStateException("Unknown Kramerius version: " + krameriusVersion);
    }

    private WebClient v5Client() {
        return WebClient.builder()
                .baseUrl(
                        UriComponentsBuilder.fromUriString(krameriusUrl)
                                .path("/search/api/v5.0/item")
                                .build()
                                .toUriString())
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(MAX_MEMORY_SIZE))
                .build();
    }

    private WebClient v7Client() {
        return WebClient.builder()
                .baseUrl(
                        UriComponentsBuilder.fromUriString(krameriusUrl)
                                .path("/search/api/client/v7.0")
                                .build()
                                .toUriString())
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(MAX_MEMORY_SIZE))
                .build();
    }
}

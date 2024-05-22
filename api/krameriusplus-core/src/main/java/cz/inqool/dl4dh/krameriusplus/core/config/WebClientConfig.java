package cz.inqool.dl4dh.krameriusplus.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    public static final String UD_PIPE_WEB_CLIENT = "UD_PIPE_WEB_CLIENT";

    public static final String NAME_TAG_WEB_CLIENT = "NAME_TAG_WEB_CLIENT";

    public static final String TEI_WEB_CLIENT = "TEI_WEB_CLIENT";

    private static final int MAX_MEMORY_SIZE = 16777216;

    @Bean(UD_PIPE_WEB_CLIENT)
    public WebClient webClient(@Value("${system.enrichment.udpipe.api:http://lindat.mff.cuni.cz/services/udpipe/api/process}")
                                       String udPipeUrl) {
        return WebClient.builder()
                .baseUrl(udPipeUrl)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(MAX_MEMORY_SIZE))
                .build();
    }

    @Bean(name = NAME_TAG_WEB_CLIENT)
    public WebClient webClientNameTag(@Value("${system.enrichment.nametag.api:http://lindat.mff.cuni.cz/services/nametag/api/recognize}")
                                              String nameTagUrl) {
        return WebClient.builder()
                .baseUrl(nameTagUrl)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(MAX_MEMORY_SIZE))
                .build();

    }

    @Bean(name = TEI_WEB_CLIENT)
    public WebClient webClientTei(@Value("${system.enrichment.tei.api:http://localhost:5000/tei}") String teiApi) {
        return WebClient.builder()
                // logging request bodies
//                .clientConnector(new ReactorClientHttpConnector(HttpClient
//                        .create()
//                .wiretap("reactor.netty.http.client.HttpClient",
//                        LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);))
                .baseUrl(teiApi)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(MAX_MEMORY_SIZE))
//                .codecs(configurer -> {
//                    configurer.defaultCodecs().maxInMemorySize(MAX_MEMORY_SIZE);
//                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
//                    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
//                })
                .build();
    }
}

package cz.inqool.dl4dh.krameriusplus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final int MAX_MEMORY_SIZE = 16777216;

    @Bean(name = "krameriusWebClient")
    public WebClient webClientKramerius(@Value("${kramerius.api:https://kramerius.mzk.cz}") String krameriusApi) {
        return WebClient
                .builder()
                .baseUrl(krameriusApi + "/search/api/v5.0/item")
                .exchangeStrategies(ExchangeStrategies.builder().codecs((configurer) -> {
                    configurer.defaultCodecs().jaxb2Encoder(new Jaxb2XmlEncoder());
                    configurer.defaultCodecs().jaxb2Decoder(new Jaxb2XmlDecoder(MimeTypeUtils.TEXT_XML, MimeTypeUtils.TEXT_PLAIN));
                }).build())
                .build();
    }

    @Bean(name = "udPipeWebClient")
    public WebClient webClientUDPipe(@Value("${enrichment.udpipe.api:http://lindat.mff.cuni.cz/services/udpipe/api/process}")
                                                 String udPipeApi) {
        return WebClient
                .builder()
                .baseUrl(udPipeApi)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(MAX_MEMORY_SIZE))
                .build();
    }

    @Bean(name = "nameTagWebClient")
    public WebClient webClientNameTag(@Value("${enrichment.nametag.api:http://lindat.mff.cuni.cz/services/nametag/api/recognize}")
                                                  String nameTagApi) {
        return WebClient
                .builder()
                .baseUrl(nameTagApi)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(MAX_MEMORY_SIZE))
                .build();
    }

    @Bean(name = "teiWebClient")
    public WebClient webClientTei(@Value("${enrichment.tei.api:http://localhost:5000}") String teiApi) {
        return WebClient
                .builder()
                .baseUrl(teiApi)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(MAX_MEMORY_SIZE))
                .build();
    }
}

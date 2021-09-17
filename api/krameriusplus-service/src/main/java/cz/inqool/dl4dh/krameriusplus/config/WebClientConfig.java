package cz.inqool.dl4dh.krameriusplus.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.*;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
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

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            log.info(clientRequest.body().toString());
            return Mono.just(clientRequest);
        });
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
    public WebClient webClientTei(@Value("${enrichment.tei.api:http://localhost:5000/tei}") String teiApi) {
        return WebClient
                .builder()
                .baseUrl(teiApi)
                // enable for logging
//                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)))
//                .filters(exchangeFilterFunctions -> {
//                    exchangeFilterFunctions.add(logRequest());
//                })
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(MAX_MEMORY_SIZE))
                .build();
    }

    @Bean
    public RestTemplate getRestTemplate(@Value("${enrichment.tei.api:http://localhost:5000/tei}") String teiApi) {
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

        restTemplate.setErrorHandler(new BodyLoggingResponseErrorHandler());

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new LoggingRequestInterceptor());
        restTemplate.setInterceptors(interceptors);
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(teiApi));

        return restTemplate;
    }
}

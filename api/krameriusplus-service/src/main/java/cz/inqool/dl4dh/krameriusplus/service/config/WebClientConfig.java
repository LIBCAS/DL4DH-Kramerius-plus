package cz.inqool.dl4dh.krameriusplus.service.config;

import cz.inqool.dl4dh.krameriusplus.core.config.BodyLoggingResponseErrorHandler;
import cz.inqool.dl4dh.krameriusplus.core.config.KrameriusInfo;
import cz.inqool.dl4dh.krameriusplus.core.config.LoggingRequestInterceptor;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Configuration
@Slf4j
public class WebClientConfig {

    private static final int MAX_MEMORY_SIZE = 16777216;

    private WebClient getWebClient(String url) throws SSLException {
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)) // TODO: UNSAFE!! enable ssl
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(MAX_MEMORY_SIZE))
                .baseUrl(url)
                .exchangeStrategies(ExchangeStrategies.builder().codecs((configurer) -> {
                    configurer.defaultCodecs().jaxb2Encoder(new Jaxb2XmlEncoder());
                    configurer.defaultCodecs().jaxb2Decoder(new Jaxb2XmlDecoder(MimeTypeUtils.TEXT_XML, MimeTypeUtils.TEXT_PLAIN)); })
                        .build())
                .build();
    }

    @Bean
    public KrameriusInfo krameriusInfo(@Value("${system.kramerius.code}") String krameriusCode) {
        String uri = UriComponentsBuilder
                .fromHttpUrl("https://registr.digitalniknihovna.cz/libraries/")
                .path(krameriusCode + ".json")
                .build()
                .toUriString();

        WebClient webClient = WebClient.builder().build();
        Map<String, Object> krameriusInfos = webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        notNull(krameriusInfos, () -> new IllegalStateException("GET to '" + uri +"' did not return any results"));

        return new KrameriusInfo(krameriusInfos);
    }

    @Bean(name = "krameriusWebClient")
    public WebClient webClientKramerius(KrameriusInfo krameriusInfo) throws SSLException {
        return getWebClient(krameriusInfo.getInfo().get("url") + "/search/api/v5.0/item");
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
    public WebClient webClientUDPipe(@Value("${system.enrichment.udpipe.api:http://lindat.mff.cuni.cz/services/udpipe/api/process}")
                                             String udPipeApi) throws SSLException {
        return getWebClient(udPipeApi);
    }

    @Bean(name = "nameTagWebClient")
    public WebClient webClientNameTag(@Value("${system.enrichment.nametag.api:http://lindat.mff.cuni.cz/services/nametag/api/recognize}")
                                              String nameTagApi) throws SSLException {
        return getWebClient(nameTagApi);
    }

    @Bean(name = "teiWebClient")
    public WebClient webClientTei(@Value("${system.enrichment.tei.api:http://localhost:5000/tei}") String teiApi) throws SSLException {
        return getWebClient(teiApi);
    }

    @Bean
    public RestTemplate getRestTemplate(@Value("${system.enrichment.tei.api:http://localhost:5000/tei}") String teiApi,
                                        LoggingRequestInterceptor loggingRequestInterceptor) {
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

        restTemplate.setErrorHandler(new BodyLoggingResponseErrorHandler());

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(loggingRequestInterceptor);
        restTemplate.setInterceptors(interceptors);
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(teiApi));

        return restTemplate;
    }
}

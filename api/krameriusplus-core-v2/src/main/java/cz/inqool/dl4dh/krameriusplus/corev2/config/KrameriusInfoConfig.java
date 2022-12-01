package cz.inqool.dl4dh.krameriusplus.corev2.config;

import cz.inqool.dl4dh.krameriusplus.api.KrameriusInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.notNull;

@Configuration
@Slf4j
public class KrameriusInfoConfig {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE_REF = new ParameterizedTypeReference<>() {
    };

    @Value("${system.kramerius.code}")
    private String krameriusCode;

    @Value("${system.kramerius.default-url:}")
    private String krameriusDefaultUrl;

    @Value("${system.kramerius.register-url}")
    private String krameriusRegisterUrl;

    @Bean
    public KrameriusInfo krameriusInfo() {
        String endpoint = UriComponentsBuilder
                .fromUriString(krameriusDefaultUrl)
                .pathSegment(krameriusCode)
                .toUriString();

        WebClient webClient = WebClient.create(endpoint);

        Map<String, Object> krameriusInfoMap = new HashMap<>();
        try {
            krameriusInfoMap = webClient
                    .get()
                    .retrieve()
                    .bodyToMono(MAP_TYPE_REF)
                    .block();

            notNull(krameriusInfoMap, () -> new IllegalStateException("Request to '" + endpoint + "' did not return any results."));
        } catch (Exception e) {
            if (krameriusDefaultUrl == null) {
                throw new IllegalStateException("Failed to connect to " + krameriusRegisterUrl +
                        " and no default URL of Kramerius is set.");
            } else {
                krameriusInfoMap.put("url", krameriusDefaultUrl);
                log.warn("Failed to connect to {} and retrieve information about Kramerius instance. " +
                        "Default URL of Kramerius is set to {}", krameriusRegisterUrl, krameriusDefaultUrl);
            }
        }

        return new KrameriusInfo(krameriusInfoMap);
    }
}

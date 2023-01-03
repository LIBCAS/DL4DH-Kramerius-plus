package cz.inqool.dl4dh.krameriusplus.corev2.config;

import cz.inqool.dl4dh.krameriusplus.api.KrameriusInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private KrameriusProperties krameriusProperties;

    @Bean
    public KrameriusInfo krameriusInfo() {
        String endpoint = UriComponentsBuilder
                .fromUriString(krameriusProperties.getRegisterUrl())
                .pathSegment(krameriusProperties.getCode())
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
            if (krameriusProperties.getDefaultUrl() == null) {
                throw new IllegalStateException("Failed to connect to " + krameriusProperties.getRegisterUrl() +
                        " and no default URL of Kramerius is set.");
            } else {
                krameriusInfoMap.put("url", krameriusProperties.getDefaultUrl());
                log.warn("Failed to connect to {} and retrieve information about Kramerius instance. " +
                        "Default URL of Kramerius is set to {}", krameriusProperties.getRegisterUrl(), krameriusProperties.getDefaultUrl());
            }
        }

        return new KrameriusInfo(krameriusInfoMap);
    }

    @Autowired
    public void setKrameriusProperties(KrameriusProperties krameriusProperties) {
        this.krameriusProperties = krameriusProperties;
    }
}

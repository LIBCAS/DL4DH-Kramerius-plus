package cz.inqool.dl4dh.krameriusplus.core.config;

import cz.inqool.dl4dh.krameriusplus.api.KrameriusInfo;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KrameriusInfoConfig {

    @Bean
    public KrameriusInfo krameriusInfo(MockWebServer mockWebServer) {
        Map<String, Object> krameriusInfoMap = new HashMap<>();
        krameriusInfoMap.put("url", mockWebServer.url("/").toString());

        return new KrameriusInfo(krameriusInfoMap);
    }
}

package cz.inqool.dl4dh.krameriusplus.core.config;

import cz.inqool.dl4dh.krameriusplus.api.KrameriusInfo;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KrameriusInfoConfig {

    @Value("${system.kramerius.version}")
    private String krameriusVersion;

    @Bean
    public KrameriusInfo krameriusInfo(MockWebServer mockWebServer) {
        Map<String, Object> krameriusInfoMap = new HashMap<>();
        krameriusInfoMap.put("url", mockWebServer.url("/").toString());
        krameriusInfoMap.put("version", krameriusVersion);

        return new KrameriusInfo(krameriusInfoMap);
    }
}

package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.core.config.KrameriusInstance;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/info")
public class InfoApi {

    private final BuildProperties buildProperties;

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Autowired
    public InfoApi(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Operation(summary = "Get current instance")
    @GetMapping("/kramerius")
    public ResponseEntity<?> getCurrentInstance(@Value("${system.kramerius}") KrameriusInstance krameriusInstance) {
        return ResponseEntity.ok(
                Map.of(
                        "instance", krameriusInstance,
                        "url", krameriusInstance.getUrl()));
    }

    @GetMapping("/version")
    public Map<String, String> getCurrentVersion() {
        return Map.of("version", buildProperties.getVersion(),
                "time", formatter.format(LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault())));
    }

}

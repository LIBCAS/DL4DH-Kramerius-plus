package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.core.config.KrameriusInfo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final KrameriusInfo krameriusInstance;

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Autowired
    public InfoApi(BuildProperties buildProperties, KrameriusInfo krameriusInstance) {
        this.buildProperties = buildProperties;
        this.krameriusInstance = krameriusInstance;
    }

    @Operation(summary = "Get current instance")
    @GetMapping("/kramerius")
    public ResponseEntity<KrameriusInfo> getCurrentInstance() {
        return ResponseEntity.ok(krameriusInstance);
    }

    @GetMapping("/version")
    public Map<String, String> getCurrentVersion() {
        return Map.of("version", buildProperties.getVersion(),
                "time", formatter.format(LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault())));
    }

}

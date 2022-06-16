package cz.inqool.dl4dh.krameriusplus.api.rest;

import cz.inqool.dl4dh.krameriusplus.core.config.KrameriusInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Info", description = "Info")
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

    @Operation(summary = "Get information about current version, last build time of Kramerius+ and about current " +
            "instance of Kramerius, to which Kramerius+ is connected. Information is obtained from " +
            "https://registr.digitalniknihovna.cz/libraries on application startup.")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCurrentVersion() {
        Map<String, String> krameriusPlusInfo = Map.of(
                "version", buildProperties.getVersion(),
                "timeOfLastBuild", formatter.format(LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault()))
                );

        Map<String, Object> responseBody = Map.of(
                "krameriusPlus", krameriusPlusInfo,
                "kramerius", krameriusInstance.getInfo()
        );

        return ResponseEntity.ok(responseBody);
    }

}

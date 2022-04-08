package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.core.config.KrameriusInstance;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/info")
public class InfoApi {

    @Operation(summary = "Get current instance")
    @GetMapping("/kramerius")
    public ResponseEntity<?> getCurrentInstance(@Value("${system.kramerius}") KrameriusInstance krameriusInstance) {
        return ResponseEntity.ok(
                Map.of(
                        "instance", krameriusInstance,
                        "url", krameriusInstance.getUrl()));
    }
}

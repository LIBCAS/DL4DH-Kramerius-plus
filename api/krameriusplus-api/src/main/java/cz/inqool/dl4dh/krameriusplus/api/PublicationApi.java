package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Monograph;
import cz.inqool.dl4dh.krameriusplus.service.filler.PublicationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

/**
 * @author Norbert Bodnar
 */
@RestController
@RequestMapping("api/publication")
public class PublicationApi {

    private final PublicationService publicationService;

    @Autowired
    public PublicationApi(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @Operation(summary = "Get an enriched publication by PID")
    @GetMapping
    public Monograph getEnrichedMonograph(@RequestParam(value = "pid") String pid,
                                          @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "1") int pageSize) {
        return publicationService.findMonograph(pid, PageRequest.of(page, pageSize));
    }
}

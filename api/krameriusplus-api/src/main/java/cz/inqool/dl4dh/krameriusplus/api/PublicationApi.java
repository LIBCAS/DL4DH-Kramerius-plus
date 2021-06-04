package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public Publication getEnrichedPublication(@RequestParam(value = "pid") String pid,
                                            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "1") int pageSize) {
        return publicationService.findWithPages(pid, PageRequest.of(page, pageSize));
    }
}

package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.params.Params;
import cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@RestController
@RequestMapping("/api/publication")
public class PublicationApi {

    private final PublicationService publicationService;

    @Autowired
    public PublicationApi(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @Operation(summary = "Get an enriched publication by PID")
    @PostMapping("/{id}")
    public Publication getEnrichedPublication(@PathVariable("id") String id,
                                              @RequestBody(required = false) Params params) {
        if (params == null) {
            params = new Params();
        }

        return publicationService.findWithPages(id, params);
    }

    @GetMapping("/list")
    public List<Publication> listGet() {
        return publicationService.list(new Params());
    }

    @PostMapping("/list")
    public List<Publication> list(@RequestBody(required = false) Params params) {
        if (params == null) {
            params = new Params();
            params.includeFields("title", "date", "issueNumber", "index", "partNumber", "_class", "volumeYear");
        }

        return publicationService.list(params);
    }
}

package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "Get an enriched publication by ID")
    @GetMapping("/{id}")
    public Publication getEnrichedPublication(@PathVariable("id") String id,
                                              @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "1") int pageSize) {
        return publicationService.findWithPages(id, PageRequest.of(page, pageSize));
    }

    @Operation(summary = "Get a list of all enriched publications")
    @GetMapping("/list")
    public List<Publication> list() {
        return publicationService.list();
    }
}

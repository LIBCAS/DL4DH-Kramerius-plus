package cz.inqool.dl4dh.krameriusplus.api.restapi;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public Publication find(@PathVariable("id") String publicationId) {
        return publicationService.find(publicationId);
    }

    @GetMapping("/list")
    public QueryResults<Publication> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return publicationService.list(page, pageSize);
    }
}

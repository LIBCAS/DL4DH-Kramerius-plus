package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
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

    @GetMapping("/{id}")
    public Publication find(@PathVariable("id") String publicationId) {
        return publicationService.find(publicationId);
    }

    @PostMapping("/list")
    public List<Publication> list(@RequestBody(required = false) Params params) {
        if (params == null) {
            params = new Params();
            params.includeFields("title", "date", "issueNumber", "index", "partNumber", "volumeYear");
        }

        return publicationService.list(params);
    }
}

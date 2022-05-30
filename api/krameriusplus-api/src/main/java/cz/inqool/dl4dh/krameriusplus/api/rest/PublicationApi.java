package cz.inqool.dl4dh.krameriusplus.api.rest;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Norbert Bodnar
 */
@Tag(name = "Publication", description = "Publikace")
@RestController
@RequestMapping("/api/publications")
public class PublicationApi {

    private final PublicationService publicationService;

    @Autowired
    public PublicationApi(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @Operation(summary = "Get detail of given publication. The returned publication does not contain child objects or pages.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Publication with given ID not found.")
    @GetMapping("/{id}")
    public Publication find(@PathVariable("id") String publicationId) {
        return publicationService.find(publicationId);
    }

    @Operation(summary = "List child publications for given parent publication.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{id}/children")
    public QueryResults<Publication> listChildren(@PathVariable("id") String publicationId,
                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return publicationService.listChildren(publicationId, page, pageSize);
    }

    @Operation(summary = "List publications.")
    @GetMapping("/list")
    public QueryResults<Publication> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return publicationService.list(page, pageSize);
    }

    @Operation(summary = "List pages for given publication.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{id}/pages")
    public QueryResults<Page> listPages(@PathVariable("id") String publicationId,
                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return publicationService.listPages(publicationId, page, pageSize);
    }
}

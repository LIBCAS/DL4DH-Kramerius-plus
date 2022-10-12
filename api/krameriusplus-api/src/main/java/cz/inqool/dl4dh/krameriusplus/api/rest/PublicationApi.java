package cz.inqool.dl4dh.krameriusplus.api.rest;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.dto.PublicationListFilterDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException.ErrorCode.INVALID_PARAMETERS;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

/**
 * @author Norbert Bodnar
 */
@Tag(name = "Publication", description = "Publikace")
@RestController
@RequestMapping("/api/publications")
public class PublicationApi {

    private final PublicationService publicationService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault());

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
        return publicationService.findAllChildren(publicationId, page, pageSize);
    }

    @Operation(summary = "List publications.")
    @GetMapping("/list")
    public QueryResults<Publication> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                          @RequestParam(value = "title", required = false) String title,
                                          @RequestParam(value = "parentId", required = false) String parentId,
                                          @RequestParam(value = "model", required = false) String model,
                                          @RequestParam(value = "isRootEnrichment", required = false) Boolean isRootEnrichment,
                                          @RequestParam(value = "createdBefore", required = false)
                                          @DateTimeFormat(iso = DATE_TIME) Instant createdBefore,
                                          @RequestParam(value = "createdAfter", required = false)
                                          @DateTimeFormat(iso = DATE_TIME) Instant createdAfter,
                                          @RequestParam(value = "isPublished", required = false) Boolean isPublished,
                                          @RequestParam(value = "publishedBefore", required = false)
                                          @DateTimeFormat(iso = DATE_TIME) Instant publishedBefore,
                                          @RequestParam(value = "publishedAfter", required = false)
                                          @DateTimeFormat(iso = DATE_TIME) Instant publishedAfter) {
        return publicationService.findAll(new PublicationListFilterDto(
                        title, parentId, model, isRootEnrichment, createdBefore, createdAfter,
                        isPublished, publishedBefore, publishedAfter),
                page, pageSize);
    }

    @Operation(summary = "List publications, which has changed its published status after the given date.")
    @GetMapping("/list/published")
    public List<Publication> listPublishedModified(@RequestParam("modifiedAfter")
                                                   @Schema(description = "DateTime in ISO format") String publishedModifiedAfter) {
        try {
            Instant instant = Instant.from(formatter.parse(publishedModifiedAfter));

            return publicationService.findAllPublishedModified(instant);
        } catch (DateTimeException e) {
            throw new ValidationException("Failed to parse given dateTime", INVALID_PARAMETERS, e);
        }
    }

    @Operation(summary = "List pages for given publication. Pages do not contain tokens and nameTagMetadata.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{id}/pages")
    public QueryResults<Page> listPages(@PathVariable("id") String publicationId,
                                                                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return publicationService.findAllPages(publicationId, page, pageSize);
    }

    @Operation(summary = "Find page for given publication by id. Page contains all metadata.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{id}/pages/{pageId}")
    public Page findPage(@PathVariable("id") String publicationId,
                         @PathVariable("pageId") String pageId) {
        return publicationService.findPage(publicationId, pageId);
    }

    @Operation(summary = "Mark publication as published.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Publication not found")
    @PostMapping("/{id}/publish")
    public void publish(@PathVariable("id") String publicationId) {
        publicationService.publish(publicationId);
    }

    @Operation(summary = "Mark publication as unpublished.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Publication not found")
    @PostMapping("/{id}/unpublish")
    public void unpublish(@PathVariable("id") String publicationId) {
        publicationService.unPublish(publicationId);
    }
}

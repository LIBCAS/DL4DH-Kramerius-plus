package cz.inqool.dl4dh.krameriusplus.rest.controller;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.api.publication.PublicationFacade;
import cz.inqool.dl4dh.krameriusplus.api.publication.PublicationFilter;
import cz.inqool.dl4dh.krameriusplus.api.publication.object.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

/**
 * @author Norbert Bodnar
 */
@Tag(name = "Publication", description = "Publikace")
@RestController
@RequestMapping("/api/publications")
public class PublicationApi {

    private final PublicationFacade facade;

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault());

    @Autowired
    public PublicationApi(PublicationFacade facade) {
        this.facade = facade;
    }

    @Operation(summary = "Get detail of given publication. The returned publication does not contain child objects or pages.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Publication with given ID not found.")
    @GetMapping("/{id}")
    public PublicationDto find(@PathVariable("id") String publicationId) {
        return facade.findPublication(publicationId);
    }

    @Operation(summary = "List child publications for given parent publication.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{id}/children")
    public List<PublicationDto> listChildren(@PathVariable("id") String publicationId) {
        return facade.listPublicationChildren(publicationId);
    }

    @Operation(summary = "List publications.")
    @GetMapping("/list")
    public Result<PublicationDto> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                       @RequestParam(value = "uuid", required = false) String uuid,
                                       @RequestParam(value = "title", required = false) String title,
                                       @RequestParam(value = "parentId", required = false) String parentId,
                                       @RequestParam(value = "model", required = false) KrameriusModel model,
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
        return facade.listPublications(new PublicationFilter(
                        uuid, title, parentId, model, isRootEnrichment, createdBefore, createdAfter,
                        isPublished, publishedBefore, publishedAfter),
                page, pageSize);
    }

    @Operation(summary = "List publications, which has changed its published status after the given date.")
    @GetMapping("/list/published")
    public Result<PublicationDto> listPublishedModified(@RequestParam("modifiedAfter")
                                                      @DateTimeFormat(iso = DATE_TIME)
                                                      @Schema(description = "DateTime in ISO format") LocalDateTime publishedModifiedAfter,
                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return facade.listPublishedModified(publishedModifiedAfter, page, pageSize);
    }

    @Operation(summary = "List pages for given publication. Pages do not contain tokens and nameTagMetadata.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{id}/pages")
    public Result<PageDto> listPages(@PathVariable("id") String publicationId,
                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return facade.listPages(publicationId, page, pageSize);
    }

    @Operation(summary = "Find page for given publication by id. Page contains all metadata.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/pages/{pageId}")
    public PageDto findPage(@PathVariable("pageId") String pageId) {
        return facade.findPage(pageId);
    }

    @Operation(summary = "Mark publication as published.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Publication not found")
    @PostMapping("/{id}/publish")
    public void publish(@PathVariable("id") String publicationId) {
        facade.publish(publicationId);
    }

    @Operation(summary = "Mark publication as unpublished.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Publication not found")
    @PostMapping("/{id}/unpublish")
    public void unpublish(@PathVariable("id") String publicationId) {
        facade.unpublish(publicationId);
    }
}

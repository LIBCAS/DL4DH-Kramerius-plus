package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.EqFilter;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.GtFilter;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.LtFilter;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.RegexFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class PublicationListFilterDto {

    private String title;

    private String parentId;

    private Instant createdBefore;

    private Instant createdAfter;

    private Boolean isPublished;

    private Instant publishedBefore;

    private Instant publishedAfter;

    public Params toParams() {
        Params params = new Params();

        if (title != null) {
            params.addFilters(new RegexFilter("title", title));
        }
        if (parentId != null) {
            params.addFilters(new EqFilter("parentId", parentId));
        }
        if (createdBefore != null) {
            params.addFilters(new LtFilter("created", createdBefore));
        }
        if (createdAfter != null) {
            params.addFilters(new GtFilter("created", createdAfter));
        }
        if (isPublished != null) {
            params.addFilters(new EqFilter("publishInfo.isPublished", isPublished));
        }
        if (publishedBefore != null) {
            params.addFilters(new LtFilter("publishInfo.publishedLastModified", publishedBefore));
        }
        if (publishedAfter != null) {
            params.addFilters(new GtFilter("publishInfo.publishedLastModifier", publishedAfter));
        }

        return params;
    }
}

package cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication;

import cz.inqool.dl4dh.krameriusplus.api.publication.PublishInfo;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsMetadata;
import cz.inqool.dl4dh.krameriusplus.api.publication.object.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.paradata.EnrichmentParadata;
import cz.inqool.dl4dh.krameriusplus.api.publication.paradata.ExternalSystem;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapperVisitor;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Document(collection = "publications")
public abstract class Publication extends DigitalObject {

    /**
     * Child publications. Can be manually pulled from DB based on {@code DomainObject#parentId}. Child publications
     * must be saved manually.
     */
    @Transient
    private List<Publication> children = new ArrayList<>();

    /**
     * Pages can be pulled from DB based on {@code DomainObject#parentId}. Pages must be saved manually.
     */
    @Transient
    private List<Page> pages = new ArrayList<>();

    private PublishInfo publishInfo = new PublishInfo();

    private String rootTitle;

    private List<String> collections = new ArrayList<>();

    private String policy;

    private ModsMetadata modsMetadata;

    private Map<ExternalSystem, EnrichmentParadata> paradata = new HashMap<>();

    private boolean pdf;

    private String teiHeaderFileId;

    private List<String> donator = new ArrayList<>();

    private Integer pageCount;

    /**
     * Flag for deciding, if this publication should be shown in grid. Should be set to true
     * for publications, which don't have parentId or their parent has not yet been enriched
     */
    private Boolean isRootEnrichment = false;

    public abstract PublicationDto accept(DigitalObjectMapperVisitor visitor);
}

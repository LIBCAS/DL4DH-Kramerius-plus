package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.EnrichmentParadata;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.ExternalSystem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document(collection = "publications")
public abstract class Publication extends DigitalObject {

    /**
     * Child publications. Can be manually pulled from DB based on {@code DomainObject#parentId}. Child publications
     * must be saved manually.
     */
    @Transient
    private List<DigitalObject> children = new ArrayList<>();

    /**
     * Pages can be pulled from DB based on {@code DomainObject#parentId}. Pages must be saved manually.
     */
    @Transient
    private List<Page> pages = new ArrayList<>();

    private PublishInfo publishInfo;

    private String title;

    private List<String> collections;

    private String policy;

    private ModsMetadata modsMetadata;

    private Map<ExternalSystem, EnrichmentParadata> paradata = new HashMap<>();

    private boolean pdf;

    @JsonProperty("pdf")
    public void unpackUrl(Map<String, Object> pdfJson) {
        this.pdf = !pdfJson.isEmpty();
    }

    @JsonIgnore
    private String ndkDirPath;

    @JsonIgnore
    private String teiHeaderFileId;

    private List<String> donator = new ArrayList<>();

    private Long pageCount;

    @JsonProperty("root_pid")
    private String rootId;

    /**
     * Flag for deciding, if this publication should be shown in grid. Should be set to true
     * for publications, which don't have parentId or their parent has not yet been enriched
     */
    @JsonIgnore
    private boolean isRootEnrichment = false;

    @JsonProperty("donator")
    public void unpackDonator(Object donators) {
        if (donators != null && donators.getClass().equals(String.class)) {
            donator.add((String) donators);
        } else if (donators instanceof Collection) {
            donator.addAll(((List<?>) donators).stream()
                    .map(obj -> (String) obj)
                    .collect(Collectors.toList()));
        }
    }
}


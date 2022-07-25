package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication;

import com.fasterxml.jackson.annotation.JsonAlias;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String url;

    @JsonAlias({"pdf", "epub"})
    public void unpackUrl(Map<String, Object> pdfJson) {
        this.url = (String) pdfJson.get("url");
    }

    @JsonIgnore
    private String ndkDirPath;

    @JsonIgnore
    private String teiHeaderFileId;
}

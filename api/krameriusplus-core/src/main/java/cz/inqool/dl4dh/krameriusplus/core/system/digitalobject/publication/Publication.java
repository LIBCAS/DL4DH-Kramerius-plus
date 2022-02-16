package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.NameTagParadata;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.OCRParadata;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.UDPipeParadata;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document(collection = "publications")
public abstract class Publication extends DigitalObject {

    @Transient
    private List<DigitalObject> children = new ArrayList<>();

    @Transient
    private List<Page> pages = new ArrayList<>();

    private String title;

    private List<String> collections;

    private String policy;

    private ModsMetadata modsMetadata;

    private OCRParadata ocrParadata;

    private UDPipeParadata udPipeParadata;

    private NameTagParadata nameTagParadata;

    private boolean hasPages = false;

    @Transient
    @JsonIgnore
    private Path ndkDir;

    @JsonIgnore
    private String teiHeader;
}

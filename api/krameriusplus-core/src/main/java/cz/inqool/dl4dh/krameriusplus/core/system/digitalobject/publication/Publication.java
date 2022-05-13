package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.Supplement;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.periodical.Periodical;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.periodical.PeriodicalItem;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.periodical.PeriodicalVolume;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.NameTagParadata;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.OCRParadata;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.UDPipeParadata;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.KrameriusModel.*;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "model")
@JsonSubTypes({
        @JsonSubTypes.Type(name = MONOGRAPH, value = Monograph.class),
        @JsonSubTypes.Type(name = MONOGRAPH_UNIT, value = MonographUnit.class),
        @JsonSubTypes.Type(name = PERIODICAL, value = Periodical.class),
        @JsonSubTypes.Type(name = PERIODICAL_VOLUME, value = PeriodicalVolume.class),
        @JsonSubTypes.Type(name = PERIODICAL_ITEM, value = PeriodicalItem.class),
        @JsonSubTypes.Type(name = SUPPLEMENT, value = Supplement.class)
})
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

    private String title;

    private List<String> collections;

    private String policy;

    private ModsMetadata modsMetadata;

    private OCRParadata ocrParadata;

    private UDPipeParadata udPipeParadata;

    private NameTagParadata nameTagParadata;

    @JsonIgnore
    private String ndkDirPath;

    @JsonIgnore
    private String teiHeaderFileId;

    /**
     * Model is lost when deserializing generic collections, therefore we need to include
     * model like this
     */
    public abstract String getModel();
}

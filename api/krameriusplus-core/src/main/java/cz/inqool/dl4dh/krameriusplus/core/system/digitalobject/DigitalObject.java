package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.internalpart.InternalPart;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.periodical.Periodical;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.periodical.PeriodicalItem;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.periodical.PeriodicalVolume;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.KrameriusModel.*;

/**
 * Base class with persistent ID field. ID of the stored enriched objects should be the same as ID of non-enriched
 * object in Kramerius
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "model", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = Monograph.class, name = MONOGRAPH),
        @JsonSubTypes.Type(value = MonographUnit.class, name = MONOGRAPH_UNIT),
        @JsonSubTypes.Type(value = Periodical.class, name = PERIODICAL),
        @JsonSubTypes.Type(value = PeriodicalVolume.class, name = PERIODICAL_VOLUME),
        @JsonSubTypes.Type(value = PeriodicalItem.class, name = PERIODICAL_ITEM),
        @JsonSubTypes.Type(value = Page.class, name = PAGE),
        @JsonSubTypes.Type(value = InternalPart.class, name = INTERNAL_PART),
        @JsonSubTypes.Type(value = Supplement.class, name = SUPPLEMENT)
})
@EqualsAndHashCode(callSuper = true)
public abstract class DigitalObject extends DatedObject {

    @JsonIgnore
    private String parentId;

    @Indexed
    private Integer index;

    private List<DigitalObjectContext> context = new ArrayList<>();

    @JsonProperty("context")
    public void unpackContext(List<List<DigitalObjectContext>> context) {
        if (context == null) {
            return;
        }

        if (context.size() > 1) {
            throw new IllegalStateException("When can context have more than 1 inner array?");
        }

        List<DigitalObjectContext> contextList = context.get(0);

        this.context = new ArrayList<>(contextList);

        if (contextList.size() >= 2) {
            // set parentId from context because otherwise root objects in enrichment have parentId set to null
            this.parentId = contextList.get(contextList.size() - 2).getPid();
        }
    }

    public abstract String getModel();
}

package cz.inqool.dl4dh.krameriusplus.domain.entity;

import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class with persistent ID field. ID of the stored enriched objects should be the same as ID of non-enriched
 * object in Kramerius
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public abstract class KrameriusObject extends DomainObject {

    abstract public KrameriusModel getModel();
}

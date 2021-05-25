package cz.inqool.dl4dh.krameriusplus.dto;

import cz.inqool.dl4dh.krameriusplus.domain.entity.DomainObject;

/**
 * Interface that every dto class that implements a model from Kramerius should implement.
 *
 * @author Norbert Bodnar
 */
public interface KrameriusDto<T extends DomainObject> {

    T toEntity();
}

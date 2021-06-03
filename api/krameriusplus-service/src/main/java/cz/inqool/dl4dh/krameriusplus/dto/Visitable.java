package cz.inqool.dl4dh.krameriusplus.dto;

import cz.inqool.dl4dh.krameriusplus.domain.entity.DomainObject;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.PublicationAssemblerVisitor;

/**
 * @author Norbert Bodnar
 */
public interface Visitable<T extends DomainObject> {

    T accept(PublicationAssemblerVisitor visitor);
}

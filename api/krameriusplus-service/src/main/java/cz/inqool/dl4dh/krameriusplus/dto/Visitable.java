package cz.inqool.dl4dh.krameriusplus.dto;

import cz.inqool.dl4dh.krameriusplus.domain.entity.DomainObject;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.KrameriusPublicationAssemblerVisitor;

/**
 * @author Norbert Bodnar
 */
public interface Visitable<T extends DomainObject> {

    T accept(KrameriusPublicationAssemblerVisitor visitor);
}

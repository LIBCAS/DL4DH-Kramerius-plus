package cz.inqool.dl4dh.krameriusplus.dto;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.KrameriusPublicationAssemblerVisitor;

/**
 * @author Norbert Bodnar
 */
public interface Visitable<T extends DigitalObject> {

    T accept(KrameriusPublicationAssemblerVisitor visitor);
}

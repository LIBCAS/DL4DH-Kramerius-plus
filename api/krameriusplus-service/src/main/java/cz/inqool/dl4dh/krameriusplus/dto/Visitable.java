package cz.inqool.dl4dh.krameriusplus.dto;

import cz.inqool.dl4dh.krameriusplus.domain.entity.KrameriusObject;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.KrameriusPublicationAssemblerVisitor;

/**
 * @author Norbert Bodnar
 */
public interface Visitable<T extends KrameriusObject> {

    T accept(KrameriusPublicationAssemblerVisitor visitor);
}

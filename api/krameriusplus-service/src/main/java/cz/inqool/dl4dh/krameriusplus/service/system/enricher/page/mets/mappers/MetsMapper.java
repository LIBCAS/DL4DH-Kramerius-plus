package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.mappers;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.MetsElement;

/**
 * Mapper class to construct MetsElement objects from xml Strings
 * @param <T>
 */
public interface MetsMapper<T extends MetsElement> {

    T map(String source);

    Class<T> supports();
}

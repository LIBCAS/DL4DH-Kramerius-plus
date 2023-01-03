package cz.inqool.dl4dh.krameriusplus.core.enricher.ndk.converter;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsElement;

/**
 * Mapper class to construct MetsElement objects from xml Strings
 * @param <T>
 */
public interface MetsConverter<T extends MetsElement> {

    T convert(String source, String elementId);

    Class<T> supports();
}

package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.valueextractors;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.MetsElement;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.DomParser;
import cz.inqool.dl4dh.mets.MdSecType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO - Helper class, clients should create a new instance for every usage
 * @param <T>
 * @param <E>
 */
public abstract class NDKExtractor<T extends MetsElement, E> {

    protected final DomParser domParser;

    private final String ATTRIBUTE_ID_PREFIX;

    private final Map<String, T> result = new HashMap<>();

    public NDKExtractor(DomParser domParser, String attributeIdPrefix) {
        this.domParser = domParser;
        this.ATTRIBUTE_ID_PREFIX = attributeIdPrefix;
    }

    public Map<String, T> extract(List<MdSecType> mdSections) {
        for (MdSecType mdSection : mdSections) {
            extractXmlComplexType(mdSection);
        }
        
        return result;
    }


    private void extractXmlComplexType(MdSecType mdSection) {
        String elementId = mdSection.getID();

        if (elementId.startsWith(ATTRIBUTE_ID_PREFIX)) {
                T metsPremis = processMetadataSection(mdSection);
                metsPremis.setId(elementId);

                result.put(elementId, metsPremis);
        }
    }

    protected abstract T processMetadataSection(MdSecType mdSection);
}

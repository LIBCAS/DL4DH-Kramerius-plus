package cz.inqool.dl4dh.krameriusplus.core.enricher.ndk;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsElement;
import cz.inqool.dl4dh.krameriusplus.core.enricher.ndk.converter.MetsConverter;
import cz.inqool.dl4dh.krameriusplus.core.enricher.ndk.converter.MetsConverterMap;
import cz.inqool.dl4dh.mets.MdSecType;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class MetsExtractor {

    protected final DomParser domParser;

    private final MetsConverterMap mappers = new MetsConverterMap();

    public MetsExtractor(DomParser domParser, Set<MetsConverter<?>> mappers) {
        this.domParser = domParser;
        mappers.forEach(this.mappers::put);
    }

    public <T extends MetsElement> Map<String, T> extract(List<MdSecType> mdSections, String idAttrPrefix,
                                                          Class<T> resultType) {
        Map<String, T> result = new HashMap<>();

        for (MdSecType mdSection : mdSections) {
            String elementId = mdSection.getID();

            if (elementId.startsWith(idAttrPrefix)) {
                Node nestedXmlPart = (Node) mdSection.getMdWrap().getXmlData().getAny().get(0);
                String nestedXmlString = domParser.nodeToString(nestedXmlPart);


                result.put(elementId, mappers.getMapper(resultType)
                        .convert(nestedXmlString, elementId));

            }
        }
        
        return result;
    }
}

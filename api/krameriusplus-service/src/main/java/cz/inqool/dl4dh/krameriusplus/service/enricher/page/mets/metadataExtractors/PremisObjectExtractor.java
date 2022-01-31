package cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets.metadataExtractors;

import cz.inqool.dl4dh.krameriusplus.domain.XMLUtils;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.object.MetsPremisObjectElement;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.object.ObjectCharacteristics;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.object.ObjectIdentifier;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.DomParser;
import info.lc.xmlns.premis_v2.File;
import info.lc.xmlns.premis_v2.ObjectCharacteristicsComplexType;
import info.lc.xmlns.premis_v2.ObjectIdentifierComplexType;

import java.util.List;

/**
 * TODO
 */
public class PremisObjectExtractor extends NDKExtractor<MetsPremisObjectElement, File> {

    public PremisObjectExtractor(DomParser domParser) {
        super(domParser, "OBJ", "premis:object", File.class);
    }

    @Override
    protected MetsPremisObjectElement processXmlComplexType(File premis) {
        MetsPremisObjectElement metsPremisObjectElement = new MetsPremisObjectElement();

        metsPremisObjectElement.setObjectIdentifier(getObjectIdentifier(premis.getObjectIdentifier()));
        metsPremisObjectElement.setObjectCharacteristics(getObjectCharacteristics(premis.getObjectCharacteristics()));

        return metsPremisObjectElement;
    }

    private ObjectCharacteristics getObjectCharacteristics(List<ObjectCharacteristicsComplexType> objectCharacteristics) {
        if (objectCharacteristics.size() != 1) {
            throw new IllegalStateException("Unexpected amount of objectCharacteristics tags");
        }

        return new ObjectCharacteristics(XMLUtils.getFirstIfOnly(objectCharacteristics));
    }

    private ObjectIdentifier getObjectIdentifier(List<ObjectIdentifierComplexType> objectIdentifiers) {
        if (objectIdentifiers.size() != 1) {
            throw new IllegalStateException("Unexpected amount of objectIdentifier tags");
        }

        ObjectIdentifierComplexType objectIdentifierComplexType = objectIdentifiers.get(0);

        return new ObjectIdentifier(objectIdentifierComplexType.getObjectIdentifierType(), objectIdentifierComplexType.getObjectIdentifierValue());
    }
}

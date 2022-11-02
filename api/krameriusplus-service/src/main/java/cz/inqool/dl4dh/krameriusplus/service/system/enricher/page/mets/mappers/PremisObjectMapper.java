package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.mappers;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.XmlException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.object.MetsPremisObjectElement;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.object.ObjectCharacteristics;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.object.ObjectIdentifier;
import cz.inqool.dl4dh.krameriusplus.core.utils.XMLUtils;
import info.lc.xmlns.premis_v2.File;
import info.lc.xmlns.premis_v2.ObjectCharacteristicsComplexType;
import info.lc.xmlns.premis_v2.ObjectIdentifierComplexType;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.XmlException.ErrorCode.INVALID_NUMBER_OF_ELEMENTS;

@Component
public class PremisObjectMapper implements MetsMapper<MetsPremisObjectElement> {

    @Override
    public MetsPremisObjectElement map(String source) {
        File extracted = JAXB.unmarshal(new StringReader(source), File.class);

        MetsPremisObjectElement result = new MetsPremisObjectElement();
        result.setObjectIdentifier(getObjectIdentifier(extracted.getObjectIdentifier()));
        result.setObjectCharacteristics(getObjectCharacteristics(extracted.getObjectCharacteristics()));

        return result;
    }

    @Override
    public Class<MetsPremisObjectElement> supports() {
        return MetsPremisObjectElement.class;
    }

    private ObjectCharacteristics getObjectCharacteristics(List<ObjectCharacteristicsComplexType> objectCharacteristics) {
        if (objectCharacteristics.size() != 1) {
            throw new XmlException("Unexpected amount of objectCharacteristics tags, expected: 1, actual: " + objectCharacteristics.size(),
                    INVALID_NUMBER_OF_ELEMENTS);
        }

        return new ObjectCharacteristics(XMLUtils.getFirstIfOnly(objectCharacteristics));
    }

    private ObjectIdentifier getObjectIdentifier(List<ObjectIdentifierComplexType> objectIdentifiers) {
        if (objectIdentifiers.size() != 1) {
            throw new XmlException("Unexpected amount of objectIdentifier tags, expected: 1, actual: " + objectIdentifiers.size(),
                    INVALID_NUMBER_OF_ELEMENTS);
        }

        ObjectIdentifierComplexType objectIdentifierComplexType = objectIdentifiers.get(0);

        return new ObjectIdentifier(objectIdentifierComplexType.getObjectIdentifierType(), objectIdentifierComplexType.getObjectIdentifierValue());
    }
}

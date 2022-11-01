package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.valueextractors;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.XmlException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.object.MetsPremisObjectElement;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.object.ObjectCharacteristics;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.object.ObjectIdentifier;
import cz.inqool.dl4dh.krameriusplus.core.utils.XMLUtils;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.DomParser;
import cz.inqool.dl4dh.mets.MdSecType;
import info.lc.xmlns.premis_v2.File;
import info.lc.xmlns.premis_v2.ObjectCharacteristicsComplexType;
import info.lc.xmlns.premis_v2.ObjectIdentifierComplexType;
import info.lc.xmlns.premis_v2.PremisComplexType;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.XmlException.ErrorCode.INVALID_NUMBER_OF_ELEMENTS;

/**
 * TODO
 */
public class PremisObjectExtractor extends NDKExtractor<MetsPremisObjectElement, File> {

    public PremisObjectExtractor(DomParser domParser) {
        super(domParser, "OBJ");
    }

    @Override
    protected MetsPremisObjectElement processMetadataSection(MdSecType mdSection) {
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(mdSection.getMdWrap().getXmlData(), stringWriter);
        PremisComplexType premisComplexType = JAXB.unmarshal(new StringReader(stringWriter.toString()), PremisComplexType.class);
        File premis = ((File) premisComplexType.getObject().get(0));

        MetsPremisObjectElement metsPremisObjectElement = new MetsPremisObjectElement();

        metsPremisObjectElement.setObjectIdentifier(getObjectIdentifier(premis.getObjectIdentifier()));
        metsPremisObjectElement.setObjectCharacteristics(getObjectCharacteristics(premis.getObjectCharacteristics()));

        return metsPremisObjectElement;
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

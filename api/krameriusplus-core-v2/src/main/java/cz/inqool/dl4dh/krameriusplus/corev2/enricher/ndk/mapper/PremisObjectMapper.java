package cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk.mapper;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisObjectElement;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisObjectElement.ObjectCharacteristics;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisObjectElement.ObjectCharacteristics.CreatingApplication;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisObjectElement.ObjectCharacteristics.Fixity;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisObjectElement.ObjectCharacteristics.Format;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisObjectElement.ObjectCharacteristics.Format.FormatDesignation;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisObjectElement.ObjectCharacteristics.Format.FormatRegistry;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisObjectElement.ObjectIdentifier;
import cz.inqool.dl4dh.krameriusplus.corev2.utils.XMLUtils;
import cz.inqool.dl4dh.ndk.premis.*;
import org.mapstruct.Mapper;

import javax.xml.bind.JAXBElement;
import java.util.List;

@Mapper
public interface PremisObjectMapper extends MetsMapperBase {

    MetsPremisObjectElement convert(File xmlElement);

    default ObjectIdentifier mapIdentifiers(List<ObjectIdentifierComplexType> xmlElements) {
        ObjectIdentifierComplexType xmlElement = XMLUtils.getFirstIfOnly(xmlElements);

        ObjectIdentifier result = new ObjectIdentifier();
        result.setType(xmlElement.getObjectIdentifierType());
        result.setValue(xmlElement.getObjectIdentifierValue());

        return result;
    }

    default ObjectCharacteristics mapCharacteristics(List<ObjectCharacteristicsComplexType> xmlElements) {
        ObjectCharacteristicsComplexType xmlElement = XMLUtils.getFirstIfOnly(xmlElements);

        ObjectCharacteristics result = new ObjectCharacteristics();
        result.setCompositionLevel(xmlElement.getCompositionLevel());
        result.setFixity(mapFixity(xmlElement.getFixity()));
        result.setSize(xmlElement.getSize());
        result.setFormat(mapFormat(xmlElement.getFormat()));
        result.setCreatingApplication(mapCreatingApp(xmlElement.getCreatingApplication()));

        return result;
    }

    default Fixity mapFixity(List<FixityComplexType> xmlElements) {
        FixityComplexType xmlElement = XMLUtils.getFirstIfOnly(xmlElements);

        Fixity result = new Fixity();
        result.setMessageDigestAlgorithm(xmlElement.getMessageDigestAlgorithm());
        result.setMessageDigest(xmlElement.getMessageDigest());
        result.setMessageDigestOriginator(xmlElement.getMessageDigestOriginator());

        return result;
    }

    default Format mapFormat(List<FormatComplexType> format) {
        FormatComplexType xmlElement = XMLUtils.getFirstIfOnly(format);

        Format result = new Format();

        List<JAXBElement<?>> content = xmlElement.getContent();

        for (JAXBElement<?> child : content) {
            if (child.getValue() instanceof FormatDesignationComplexType) {
                result.setDesignation(mapDesignation((FormatDesignationComplexType) child.getValue()));
            } if (child.getValue() instanceof FormatRegistryComplexType) {
                result.setRegistry(mapRegistry((FormatRegistryComplexType) child.getValue()));
            }
        }

        return result;
    }

    FormatDesignation mapDesignation(FormatDesignationComplexType xmlElement);

    FormatRegistry mapRegistry(FormatRegistryComplexType value);


    default CreatingApplication mapCreatingApp(List<CreatingApplicationComplexType> creatingApplication) {
        CreatingApplicationComplexType xmlElement = XMLUtils.getFirstIfOnly(creatingApplication);

        CreatingApplication result = new CreatingApplication();
        List<JAXBElement<?>> content = xmlElement.getContent();

        for (JAXBElement<?> child : content) {
            String tagName = child.getName().getLocalPart();

            switch (tagName) {
                case "creatingApplicationName":
                    result.setCreatingApplicationName(child.getValue().toString());
                    break;
                case "creatingApplicationVersion":
                    result.setCreatingApplicationVersion(child.getValue().toString());
                    break;
                case "dateCreatedByApplication":
                    result.setDateCreatedByApplication(child.getValue().toString());
                    break;
            }
        }

        return result;
    }
}

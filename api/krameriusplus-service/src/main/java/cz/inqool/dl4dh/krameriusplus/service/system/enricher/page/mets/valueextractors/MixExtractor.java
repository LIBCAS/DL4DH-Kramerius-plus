package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.valueextractors;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.mix.MetsMixElement;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.DomParser;
import cz.inqool.dl4dh.mets.MdSecType;
import gov.loc.mix.v20.Mix;
import gov.loc.mix.v20.MixType;
import org.w3c.dom.Node;

import javax.xml.bind.JAXB;
import java.io.StringReader;

public class MixExtractor extends NDKExtractor<MetsMixElement, Mix> {

    public MixExtractor(DomParser domParser) {
        super(domParser, "MIX");
    }

    @Override
    protected MetsMixElement processMetadataSection(MdSecType mdSection) {
        String mixObject = domParser.nodeToString((Node) mdSection.getMdWrap().getXmlData().getAny().get(0));
        MixType xmlComplexType = JAXB.unmarshal(new StringReader(mixObject), Mix.class);

        MetsMixElement mixElement = new MetsMixElement();

        if (xmlComplexType.getBasicDigitalObjectInformation() != null) {
            mixElement.setBasicDigitalInformation(
                    new MetsMixElement.BasicDigitalInformation(xmlComplexType.getBasicDigitalObjectInformation()));
        }

        if (xmlComplexType.getBasicImageInformation() != null) {
            mixElement.setBasicImageInformation(
                    new MetsMixElement.BasicImageInformation(xmlComplexType.getBasicImageInformation()));
        }

        if (xmlComplexType.getImageCaptureMetadata() != null) {
            mixElement.setImageCaptureMetadata(
                    new MetsMixElement.ImageCaptureMetadata(xmlComplexType.getImageCaptureMetadata()));
        }

        if (xmlComplexType.getImageAssessmentMetadata() != null) {
            mixElement.setImageAssessmentMetadata(
                    new MetsMixElement.ImageAssessmentMetadata(xmlComplexType.getImageAssessmentMetadata()));
        }

        return mixElement;
    }
}

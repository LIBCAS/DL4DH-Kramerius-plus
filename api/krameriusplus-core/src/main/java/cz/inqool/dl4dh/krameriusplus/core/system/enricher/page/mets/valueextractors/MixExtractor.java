package cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.mets.valueextractors;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.mix.MetsMixElement;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.DomParser;
import gov.loc.mix.v20.Mix;

public class MixExtractor extends NDKExtractor<MetsMixElement, Mix> {

    public MixExtractor(DomParser domParser) {
        super(domParser, "MIX", "mix:mix", Mix.class);
    }

    @Override
    protected MetsMixElement processXmlComplexType(Mix xmlComplexType) {
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

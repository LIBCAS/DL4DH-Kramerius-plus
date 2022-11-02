package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.mappers;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.mix.MetsMixElement;
import gov.loc.mix.v20.MixType;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXB;
import java.io.StringReader;

@Component
public class MixMapper implements MetsMapper<MetsMixElement> {

    @Override
    public MetsMixElement map(String source) {
        MixType extracted = JAXB.unmarshal(new StringReader(source), MixType.class);

        MetsMixElement mixElement = new MetsMixElement();

        if (extracted.getBasicDigitalObjectInformation() != null) {
            mixElement.setBasicDigitalInformation(
                    new MetsMixElement.BasicDigitalInformation(extracted.getBasicDigitalObjectInformation()));
        }

        if (extracted.getBasicImageInformation() != null) {
            mixElement.setBasicImageInformation(
                    new MetsMixElement.BasicImageInformation(extracted.getBasicImageInformation()));
        }

        if (extracted.getImageCaptureMetadata() != null) {
            mixElement.setImageCaptureMetadata(
                    new MetsMixElement.ImageCaptureMetadata(extracted.getImageCaptureMetadata()));
        }

        if (extracted.getImageAssessmentMetadata() != null) {
            mixElement.setImageAssessmentMetadata(
                    new MetsMixElement.ImageAssessmentMetadata(extracted.getImageAssessmentMetadata()));
        }

        return mixElement;
    }

    @Override
    public Class<MetsMixElement> supports() {
        return MetsMixElement.class;
    }
}

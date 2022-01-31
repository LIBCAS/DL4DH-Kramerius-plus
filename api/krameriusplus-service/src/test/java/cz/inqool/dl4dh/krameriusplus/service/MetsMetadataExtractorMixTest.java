package cz.inqool.dl4dh.krameriusplus.service;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.mix.MetsMixElement;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class MetsMetadataExtractorMixTest extends MetsMetadataExtractorTest {

    @BeforeEach
    public void setUp() {
        preparePage();
    }

    @Test
    public void hasTwoMixWithCorrectIds() {
        metsExtractor.enrich(page);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(page.getMetsMetadata().getMix().get("MIX_001")).isNotNull();
            softly.assertThat(page.getMetsMetadata().getMix().get("MIX_002")).isNotNull();
            softly.assertThat(page.getMetsMetadata().getMix().size()).isEqualTo(2);
        });
    }

    @Test
    public void hasCorrectAttributes() {
        metsExtractor.enrich(page);

        MetsMixElement mix = page.getMetsMetadata().getMix().get("MIX_001");

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(mix.getBasicDigitalInformation().getCompression().size()).isEqualTo(1);
            softly.assertThat(mix.getBasicDigitalInformation().getCompression().get(0).getCompressionScheme()).isEqualTo("LZW");

            var characteristics = mix.getBasicImageInformation().getBasicImageCharacteristics();

            softly.assertThat(characteristics.getImageWidth()).isEqualTo(2000);
            softly.assertThat(characteristics.getImageHeight()).isEqualTo(3300);
            softly.assertThat(characteristics.getPhotometricInterpretation().getColorSpace()).isEqualTo("PaletteColor");

            var imageCapture = mix.getImageCaptureMetadata();

            softly.assertThat(imageCapture.getGeneralCaptureInformation().getDateTimeCreated()).isEqualTo("2018-08-22T06:32:21.596Z");
            softly.assertThat(imageCapture.getGeneralCaptureInformation().getImageProducers().size()).isEqualTo(1);
            softly.assertThat(imageCapture.getGeneralCaptureInformation().getImageProducers().get(0)).isEqualTo("KNAV");
            softly.assertThat(imageCapture.getGeneralCaptureInformation().getCaptureDevice()).isEqualTo("transmission scanner");

            softly.assertThat(imageCapture.getScannerCapture().getScannerManufacturer()).isEqualTo("Zeutschel");
            softly.assertThat(imageCapture.getScannerCapture().getScannerModel().getScannerModelName()).isEqualTo("OS");
            softly.assertThat(imageCapture.getScannerCapture().getScannerModel().getScannerModelNumber()).isEqualTo("7000-90TT");
            softly.assertThat(imageCapture.getScannerCapture().getScannerModel().getScannerModelSerialNo()).isEqualTo("50250");

            softly.assertThat(imageCapture.getScannerCapture().getMaximumOpticalResolution().getXOpticalResolution()).isEqualTo(BigInteger.valueOf(400L));
            softly.assertThat(imageCapture.getScannerCapture().getMaximumOpticalResolution().getYOpticalResolution()).isEqualTo(BigInteger.valueOf(400L));
            softly.assertThat(imageCapture.getScannerCapture().getMaximumOpticalResolution().getUnit()).isEqualTo("in.");

            softly.assertThat(imageCapture.getScannerCapture().getScannerSensor()).isEqualTo("OneChipColourArea");

            softly.assertThat(imageCapture.getScannerCapture().getScanningSystemSoftware()
                    .getScanningSystemSoftwareName()).isEqualTo("Omniscan OS-7000");
            softly.assertThat(imageCapture.getScannerCapture().getScanningSystemSoftware()
                    .getScanningSystemSoftwareVersionNo()).isEqualTo("10.0");

            var spatialMetrics = mix.getImageAssessmentMetadata().getSpatialMetrics();

            softly.assertThat(spatialMetrics.getSamplingFrequencyUnit().getValue().value()).isEqualTo("in.");
            softly.assertThat(spatialMetrics.getXSamplingFrequency().getNumerator()).isEqualTo(BigInteger.valueOf(414));
            softly.assertThat(spatialMetrics.getXSamplingFrequency().getDenominator()).isEqualTo(BigInteger.valueOf(1));
            softly.assertThat(spatialMetrics.getYSamplingFrequency().getNumerator()).isEqualTo(BigInteger.valueOf(414));
            softly.assertThat(spatialMetrics.getYSamplingFrequency().getDenominator()).isEqualTo(BigInteger.valueOf(1));


            var colorEncoding = mix.getImageAssessmentMetadata().getImageColorEncoding();

            softly.assertThat(colorEncoding.getBitsPerSample().getBitsPerSampleValue().size()).isEqualTo(1);
            softly.assertThat(colorEncoding.getBitsPerSample().getBitsPerSampleValue().get(0).getValue()).isEqualTo(BigInteger.valueOf(8L));
            softly.assertThat(colorEncoding.getBitsPerSample().getBitsPerSampleUnit().getValue().value()).isEqualTo("integer");

            softly.assertThat(colorEncoding.getSamplesPerPixel().getValue()).isEqualTo(BigInteger.valueOf(1L));
        });
    }
}

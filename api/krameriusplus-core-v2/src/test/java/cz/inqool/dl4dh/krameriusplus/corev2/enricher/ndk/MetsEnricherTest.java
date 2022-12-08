package cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMetadata;
import cz.inqool.dl4dh.krameriusplus.corev2.CoreBaseTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.math.BigInteger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetsEnricherTest extends CoreBaseTest {

    @Autowired
    private MetsEnricher enricher;

    @Autowired
    private ResourceLoader resourceLoader;

    private MetsMetadata metsMetadata;

    @BeforeAll
    void setup() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:ndk/ndk_page_mets.xml");
        metsMetadata = enricher.extractMetadata(resource.getFile().toPath());
    }

    @Test
    void premisObjectsFields() {
        SoftAssertions.assertSoftly(softly -> {
            var premisObjects = metsMetadata.getPremisObjects();

            softly.assertThat(premisObjects.size()).isEqualTo(3);

            // OBJ_001
            var premisObject = metsMetadata.getPremisObjects().get("OBJ_001");
            softly.assertThat(premisObject.getObjectIdentifier().getType()).isEqualTo("ProArc_URI");
            softly.assertThat(premisObject.getObjectIdentifier().getValue()).isEqualTo("info:fedora/uuid:aca915af-9074-4d71-8ef2-8c1e4edd1ee2/RAW");

            var characteristics = premisObject.getObjectCharacteristics();
            softly.assertThat(characteristics.getCompositionLevel()).isEqualTo(BigInteger.valueOf(0));
            softly.assertThat(characteristics.getSize()).isEqualTo(17_287_955);

            var fixity = characteristics.getFixity();
            softly.assertThat(fixity.getMessageDigestAlgorithm()).isEqualTo("MD5");
            softly.assertThat(fixity.getMessageDigest()).isEqualTo("9eaee368dba9c8b29e261fe4cca9bfad");
            softly.assertThat(fixity.getMessageDigestOriginator()).isEqualTo("ProArc");

            var format = characteristics.getFormat();
            softly.assertThat(format.getDesignation().getFormatName()).isEqualTo("image/tiff");
            softly.assertThat(format.getDesignation().getFormatVersion()).isEqualTo("1.0");
            softly.assertThat(format.getRegistry().getFormatRegistryName()).isEqualTo("PRONOM");
            softly.assertThat(format.getRegistry().getFormatRegistryKey()).isEqualTo("fmt/353");

            var creatingApp = characteristics.getCreatingApplication();
            softly.assertThat(creatingApp.getCreatingApplicationName()).isEqualTo("ProArc");
            softly.assertThat(creatingApp.getCreatingApplicationVersion()).isEqualTo("3.6.2");
            softly.assertThat(creatingApp.getDateCreatedByApplication()).isEqualTo("2021-08-19T07:13:09.332+02:00");

            // OBJ_002
            var premisObject2 = premisObjects.get("OBJ_002");
            softly.assertThat(premisObject2.getObjectIdentifier().getType()).isEqualTo("ProArc_URI");
            softly.assertThat(premisObject2.getObjectIdentifier().getValue()).isEqualTo("info:fedora/uuid:aca915af-9074-4d71-8ef2-8c1e4edd1ee2/NDK_ARCHIVAL");

            // OBJ_003
            var premisObject3 = premisObjects.get("OBJ_003");
            softly.assertThat(premisObject3.getObjectIdentifier().getType()).isEqualTo("ProArc_URI");
            softly.assertThat(premisObject3.getObjectIdentifier().getValue()).isEqualTo("info:fedora/uuid:aca915af-9074-4d71-8ef2-8c1e4edd1ee2/ALTO");
        });
    }

    @Test
    void premisAgentsFields() {
        SoftAssertions.assertSoftly(softly -> {
            var premisAgents = metsMetadata.getPremisAgents();

            softly.assertThat(premisAgents.size()).isEqualTo(1);

            var premisAgent = premisAgents.get("AGENT_001");
            softly.assertThat(premisAgent.getAgentIdentifiers().get(0).getIdentifierType()).isEqualTo("ProArc_AgentID");
            softly.assertThat(premisAgent.getAgentIdentifiers().get(0).getIdentifierValue()).isEqualTo("ProArc");
            softly.assertThat(premisAgent.getAgentType()).isEqualTo("software");
            softly.assertThat(premisAgent.getAgentNames().get(0)).isEqualTo("ProArc");
        });
    }

    @Test
    void premisEventsFields() {
        SoftAssertions.assertSoftly(softly -> {
            var premisEvents = metsMetadata.getPremisEvents();

            softly.assertThat(premisEvents.size()).isEqualTo(3);

            var premisEvent1 = premisEvents.get("EVT_001");
            softly.assertThat(premisEvent1.getLinkingAgentIdentifier().getIdentifierType()).isEqualTo("ProArc_AgentID");
            softly.assertThat(premisEvent1.getLinkingAgentIdentifier().getIdentifierValue()).isEqualTo("ProArc");
            softly.assertThat(premisEvent1.getLinkingAgentIdentifier().getRoles().get(0)).isEqualTo("software");
            softly.assertThat(premisEvent1.getEventType()).isEqualTo("capture");
            softly.assertThat(premisEvent1.getEventDateTime()).isEqualTo("2021-05-18T12:02:14.256Z");
            softly.assertThat(premisEvent1.getEventDetail()).isEqualTo("capture/digitization");
            softly.assertThat(premisEvent1.getEventOutcomeInformation().getOutcome()).isEqualTo("successful");

            var premisEvent2 = premisEvents.get("EVT_002");
            softly.assertThat(premisEvent2.getLinkingAgentIdentifier().getIdentifierType()).isEqualTo("ProArc_AgentID");
            softly.assertThat(premisEvent2.getLinkingAgentIdentifier().getIdentifierValue()).isEqualTo("ProArc");
            softly.assertThat(premisEvent2.getLinkingAgentIdentifier().getRoles().get(0)).isEqualTo("software");
            softly.assertThat(premisEvent2.getEventType()).isEqualTo("migration");
            softly.assertThat(premisEvent2.getEventDateTime()).isEqualTo("2021-05-18T12:02:14.256Z");
            softly.assertThat(premisEvent2.getEventDetail()).isEqualTo("migration/MC_creation");
            softly.assertThat(premisEvent2.getEventOutcomeInformation().getOutcome()).isEqualTo("successful");

            var premisEvent3 = premisEvents.get("EVT_003");
            softly.assertThat(premisEvent3.getLinkingAgentIdentifier().getIdentifierType()).isEqualTo("ProArc_AgentID");
            softly.assertThat(premisEvent3.getLinkingAgentIdentifier().getIdentifierValue()).isEqualTo("ProArc");
            softly.assertThat(premisEvent3.getLinkingAgentIdentifier().getRoles().get(0)).isEqualTo("software");
            softly.assertThat(premisEvent3.getEventType()).isEqualTo("capture");
            softly.assertThat(premisEvent3.getEventDateTime()).isEqualTo("2021-05-18T12:02:14.256Z");
            softly.assertThat(premisEvent3.getEventDetail()).isEqualTo("capture/XML_creation");
            softly.assertThat(premisEvent3.getEventOutcomeInformation().getOutcome()).isEqualTo("successful");
        });
    }

    @Test
    void mixFields() {
        SoftAssertions.assertSoftly(softly -> {
            var premisMixes = metsMetadata.getMix();

            softly.assertThat(premisMixes.size()).isEqualTo(2);

            var mix1 = premisMixes.get("MIX_001");
            softly.assertThat(mix1.getBasicDigitalObjectInformation().getCompression().get(0).getCompressionScheme()).isEqualTo("LZW");
            softly.assertThat(mix1.getBasicImageInformation().getBasicImageCharacteristics().getImageHeight()).isEqualTo("3500");
            softly.assertThat(mix1.getBasicImageInformation().getBasicImageCharacteristics().getImageWidth()).isEqualTo("2200");
            softly.assertThat(mix1.getBasicImageInformation().getBasicImageCharacteristics().getPhotometricInterpretation().getColorSpace()).isEqualTo("RGB");

            // TODO write this class
//            softly.assertThat(mix1.getImageAssessmentMetadata()).isEqualTo();

            softly.assertThat(mix1.getImageCaptureMetadata().getGeneralCaptureInformation().getCaptureDevice()).isEqualTo("REFLECTION_PRINT_SCANNER");
            softly.assertThat(mix1.getImageCaptureMetadata().getGeneralCaptureInformation().getImageProducers().get(0)).isEqualTo("KNAV");
            softly.assertThat(mix1.getImageCaptureMetadata().getGeneralCaptureInformation().getDateTimeCreated()).isEqualTo("2021-05-18T12:02:14.256Z");

            softly.assertThat(mix1.getImageCaptureMetadata().getScannerCapture().getScannerManufacturer()).isEqualTo("Zeutschel");
            softly.assertThat(mix1.getImageCaptureMetadata().getScannerCapture().getScannerModel().getScannerModelName()).isEqualTo("Advanced plus");
            softly.assertThat(mix1.getImageCaptureMetadata().getScannerCapture().getScannerModel().getScannerModelNumber()).isEqualTo("OS 15000");
            softly.assertThat(mix1.getImageCaptureMetadata().getScannerCapture().getScannerModel().getScannerModelSerialNo()).isEqualTo("60368");
            softly.assertThat(mix1.getImageCaptureMetadata().getScannerCapture().getScannerSensor()).isEqualTo("COLOR_SEQUENTIAL_LINEAR");
            softly.assertThat(mix1.getImageCaptureMetadata().getScannerCapture().getMaximumOpticalResolution().getYOpticalResolution()).isEqualTo(BigInteger.valueOf(600L));
            softly.assertThat(mix1.getImageCaptureMetadata().getScannerCapture().getMaximumOpticalResolution().getXOpticalResolution()).isEqualTo(BigInteger.valueOf(600L));
            softly.assertThat(mix1.getImageCaptureMetadata().getScannerCapture().getMaximumOpticalResolution().getUnit()).isEqualTo("IN");
            softly.assertThat(mix1.getImageCaptureMetadata().getScannerCapture().getScanningSystemSoftware().getScanningSoftwareName()).isEqualTo("Perfect book");
            softly.assertThat(mix1.getImageCaptureMetadata().getScannerCapture().getScanningSystemSoftware().getScanningSoftwareVersionNo()).isEqualTo("4.0");

            var mix2 = premisMixes.get("MIX_002");
            softly.assertThat(mix2.getBasicDigitalObjectInformation().getCompression().get(0).getCompressionScheme()).isEqualTo("JPEG 2000");
            softly.assertThat(mix2.getBasicImageInformation().getBasicImageCharacteristics().getImageHeight()).isEqualTo("3500");
            softly.assertThat(mix2.getBasicImageInformation().getBasicImageCharacteristics().getImageWidth()).isEqualTo("2200");
            softly.assertThat(mix2.getBasicImageInformation().getBasicImageCharacteristics().getPhotometricInterpretation().getColorSpace()).isEqualTo("RGB");

            // TODO write this class
//            softly.assertThat(mix1.getImageAssessmentMetadata()).isEqualTo();
        });
    }
}

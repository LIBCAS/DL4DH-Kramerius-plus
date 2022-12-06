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

            // TODO: OBJ_002, OBJ_003
        });
    }

    @Test
    void premisAgentsFields() {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Test
    void premisEventsFields() {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Test
    void mixFields() {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }
}

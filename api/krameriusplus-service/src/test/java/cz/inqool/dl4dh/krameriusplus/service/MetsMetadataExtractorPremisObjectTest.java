package cz.inqool.dl4dh.krameriusplus.service;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.object.ObjectCharacteristics;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.object.ObjectIdentifier;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MetsMetadataExtractorPremisObjectTest extends MetsMetadataExtractorTest {

    @BeforeEach
    public void setUp() {
        preparePage();
    }

    @Test
    public void hasThreePremisObjects() {
        metsExtractor.enrich(page);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(page.getMetsMetadata().getPremisObjects().size()).isEqualTo(3);
        });
    }

    @Test
    public void hasThreePremisObjectsWithCorrectIds() {
        metsExtractor.enrich(page);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(page.getMetsMetadata().getPremisObjects().get("OBJ_001")).isNotNull();
            softly.assertThat(page.getMetsMetadata().getPremisObjects().get("OBJ_002")).isNotNull();
            softly.assertThat(page.getMetsMetadata().getPremisObjects().get("OBJ_003")).isNotNull();
            softly.assertThat(page.getMetsMetadata().getPremisObjects().size()).isEqualTo(3);
        });
    }

    @Test
    public void isObjectIdentifierTagCorrect() {
        metsExtractor.enrich(page);

        ObjectIdentifier extractedObjectIdentifier = page.getMetsMetadata().getPremisObjects().get("OBJ_001").getObjectIdentifier();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(extractedObjectIdentifier.getType()).isEqualTo("ProArc_URI");
            softly.assertThat(extractedObjectIdentifier.getValue()).isEqualTo("info:fedora/uuid:4e85f23f-91b8-48ac-ab5d-075a41fcf876/RAW");
        });
    }

    @Test
    public void isObjectCharacteristicsTagCorrect() {
        metsExtractor.enrich(page);

        ObjectCharacteristics objectCharacteristics = page.getMetsMetadata().getPremisObjects().get("OBJ_001").getObjectCharacteristics();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(objectCharacteristics.getCompositionLevel()).isEqualTo(0);

            softly.assertThat(objectCharacteristics.getFixity().getMessageDigestAlgorithm()).isEqualTo("MD5");
            softly.assertThat(objectCharacteristics.getFixity().getMessageDigest()).isEqualTo("c5a172c2dd91ec268aa59c32e3c24b90");
            softly.assertThat(objectCharacteristics.getFixity().getMessageDigestOriginator()).isEqualTo("ProArc");

            softly.assertThat(objectCharacteristics.getSize()).isEqualTo(908368);

            softly.assertThat(objectCharacteristics.getFormat().getDesignation().getFormatName()).isEqualTo("image/tiff");
            softly.assertThat(objectCharacteristics.getFormat().getDesignation().getFormatVersion()).isEqualTo("1.0");

            softly.assertThat(objectCharacteristics.getFormat().getRegistry().getFormatRegistryName()).isEqualTo("PRONOM");
            softly.assertThat(objectCharacteristics.getFormat().getRegistry().getFormatRegistryKey()).isEqualTo("fmt/353");

            softly.assertThat(objectCharacteristics.getCreatingApplication().getCreatingApplicationName()).isEqualTo("ProArc");
            softly.assertThat(objectCharacteristics.getCreatingApplication().getCreatingApplicationVersion()).isEqualTo("3.6");
            softly.assertThat(objectCharacteristics.getCreatingApplication().getDateCreatedByApplication()).isEqualTo("2020-12-14T12:02:28.791+01:00");
        });
    }
}

package cz.inqool.dl4dh.krameriusplus.core.kramerius;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.CoreBaseTest;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.periodical.Periodical;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.periodical.PeriodicalItem;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.periodical.PeriodicalVolume;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import cz.inqool.dl4dh.mods.StringPlusLanguage;
import cz.inqool.dl4dh.mods.TitleInfoDefinition;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KrameriusMessengerTest extends CoreBaseTest {

    @Autowired
    private SyncKrameriusMessenger krameriusMessenger;

    @Autowired
    private MockWebServer mockServer;

    @Test
    void periodical() {
        DigitalObject digitalObject = testAndGetDigitalObject(KrameriusMessengerResponse.PERIODICAL_RESPONSE, Periodical.class, "uuid:319546a0-5a42-11eb-b4d1-005056827e51");
        Periodical periodical = (Periodical) digitalObject;

        testPublication(periodical, "Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne",
                "Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne",
                "uuid:319546a0-5a42-11eb-b4d1-005056827e51", "public", false);
    }

    @Test
    void periodicalChildren() {
        List<DigitalObject> digitalObjects = testAndGetChildren(KrameriusMessengerChildrenResponse.PERIODICAL_CHILDREN_RESPONSE, PeriodicalVolume.class, 2);

        List<PeriodicalVolume> periodicalVolumes = digitalObjects.stream().map(digitalObject -> ((PeriodicalVolume) digitalObject)).collect(Collectors.toList());

        periodicalVolumes.forEach(periodicalVolume ->
                testPublication(periodicalVolume, "", "Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne",
                        "uuid:319546a0-5a42-11eb-b4d1-005056827e51", "public", false));

        assertThat(periodicalVolumes.get(0).getVolumeYear()).isEqualTo("1882");
        assertThat(periodicalVolumes.get(0).getVolumeNumber()).isEqualTo("1882");
        assertThat(periodicalVolumes.get(1).getVolumeYear()).isEqualTo("1898");
        assertThat(periodicalVolumes.get(1).getVolumeNumber()).isEqualTo("1898");
    }

    @Test
    void periodicalVolume() {
        DigitalObject digitalObject = testAndGetDigitalObject(KrameriusMessengerResponse.PERIODICAL_VOLUME_RESPONSE, PeriodicalVolume.class, "uuid:986ca2f0-5aaa-11ed-8756-005056827e51");

        PeriodicalVolume periodicalVolume = (PeriodicalVolume) digitalObject;

        testPublication(periodicalVolume, "", "Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne",
                "uuid:319546a0-5a42-11eb-b4d1-005056827e51", "public", false);

        assertThat(periodicalVolume.getVolumeNumber()).isEqualTo("1882");
        assertThat(periodicalVolume.getVolumeYear()).isEqualTo("1882");
    }

    @Test
    void periodicalVolumeChildren() {
        List<DigitalObject> digitalObjects = testAndGetChildren(KrameriusMessengerChildrenResponse.PERIODICAL_VOLUME_CHILDREN_RESPONSE, PeriodicalItem.class, 1);

        PeriodicalItem periodicalItem = (PeriodicalItem) digitalObjects.get(0);
        testPublication(periodicalItem, "Protokol ... veřejné schůze Bratrstva sv. Michala v Praze dne. 5", "Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne",
                "uuid:319546a0-5a42-11eb-b4d1-005056827e51", "public", false);

        assertThat(periodicalItem.getIssueNumber()).isEqualTo("");
        assertThat(periodicalItem.getDate()).isEqualTo("1882");
        assertThat(periodicalItem.getPartNumber()).isEqualTo("5");
    }

    @Test
    void periodicalItem() {
        DigitalObject digitalObject = testAndGetDigitalObject(KrameriusMessengerResponse.PERIODICAL_ITEM_RESPONSE, PeriodicalItem.class, "uuid:e8ebdd40-4ad3-11ed-9b54-5ef3fc9bb22f");

        PeriodicalItem periodicalItem = (PeriodicalItem) digitalObject;
        testPublication(periodicalItem, "Protokol ... veřejné schůze Bratrstva sv. Michala v Praze dne. 5", "Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne",
                "uuid:319546a0-5a42-11eb-b4d1-005056827e51", "public", false);

        assertThat(periodicalItem.getIssueNumber()).isEqualTo("");
        assertThat(periodicalItem.getDate()).isEqualTo("1882");
        assertThat(periodicalItem.getPartNumber()).isEqualTo("5");
    }

    @Test
    void periodicalItemChildren() {
        List<DigitalObject> digitalObjects = testAndGetChildren(KrameriusMessengerChildrenResponse.PERIODICAL_ITEM_CHILDREN_RESPONSE, Page.class, 60);

        Page page = ((Page) digitalObjects.get(0));

        assertThat(page.getPageType()).isEqualTo("FrontCover");
        assertThat(page.getPageNumber()).contains("[1a]");
        assertThat(page.getTitle()).isEqualTo("[1a]");
    }

    @Test
    void monograph() {
        DigitalObject digitalObject = testAndGetDigitalObject(KrameriusMessengerResponse.MONOGRAPH_RESPONSE, Monograph.class, "uuid:0af541d0-06c8-11e6-a5b6-005056827e52");
        Monograph monograph = (Monograph) digitalObject;
        testPublication(monograph, "Ve škole duchovní: čisté učení spiritistické : sbírka medijních sdělení a poučení, daná od duchů všech stavů, výší a druhů",
                "Ve škole duchovní: čisté učení spiritistické : sbírka medijních sdělení a poučení, daná od duchů všech stavů, výší a druhů",
                "uuid:0af541d0-06c8-11e6-a5b6-005056827e52", "public", false);

        assertThat(monograph.getCollections().get(0)).isEqualTo("vc:8e493b6d-0847-4c4e-9b40-49f25b550acd");
    }

    @Test
    void monographChildren() {
        List<DigitalObject> digitalObjects = testAndGetChildren(KrameriusMessengerChildrenResponse.MONOGRAPH_CHILDREN_RESPONSE, Page.class, 7);

        Page page = (Page) digitalObjects.get(4);
        assertThat(page.getId()).isEqualTo("uuid:0ea88040-17a7-11e6-adec-001018b5eb5c");
        assertThat(page.getRootId()).isEqualTo("uuid:0af541d0-06c8-11e6-a5b6-005056827e52");
        assertThat(page.getPolicy()).isEqualTo("public");
        assertThat(page.getTitle()).isEqualTo("[3]");
        assertThat(page.getPageType()).isEqualTo("NormalPage");
        assertThat(page.getPageNumber()).contains("[3]");
    }

    @Test
    void monographUnit() {
        MonographUnit monographUnit = (MonographUnit) testAndGetDigitalObject(KrameriusMessengerResponse.MONOGRAPH_UNIT_RESPONSE, MonographUnit.class,
                "uuid:c29c4970-55d8-11e9-936e-005056827e52");

        testPublication(monographUnit, "V ohradě měst a městských zdech. 1", "V ohradě měst a městských zdech",
                "uuid:29dea0f0-ea9a-11e9-8d0f-005056825209", "public", false);
        assertThat(monographUnit.getPartNumber()).isEqualTo("1");
    }

    @Test
    void monographUnitWithPartTitle() {
        MonographUnit monographUnit = ((MonographUnit) testAndGetDigitalObject(KrameriusMessengerResponse.MONOGRAPH_UNIT_WITH_PART_TITLE_RESPONSE, MonographUnit.class, "uuid:87ee9e20-f07c-11e3-b72e-005056827e52"));

        testPublication(monographUnit, "Ílias. 1. Ilias", "Ílias",
                "uuid:ee0e12a0-f6b5-11e3-97df-5ef3fc9bb22f", "private", false);

        assertThat(monographUnit.getPartTitle()).isEqualTo("Ilias");
        assertThat(monographUnit.getPartNumber()).isEqualTo("1");
    }

    @Test
    void monographUnitChildren() {
        testAndGetChildren(KrameriusMessengerChildrenResponse.MONOGRAPH_UNIT_CHILDREN_RESPONSE, Page.class, 15);
    }

    @Test
    void alto() {
        mockServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(KrameriusMessengerStreamsResponse.ALTO_UPPERCASE_STRING_RESPONSE)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_XML));

        Alto alto = krameriusMessenger.getAlto("test");

        assertThat(alto.getDescription().getMeasurementUnit()).isEqualTo("pixel");
        assertThat(alto.getDescription().getOCRProcessing().get(0).getOcrProcessingStep()
                .getProcessingSoftware().getSoftwareCreator()).isEqualTo("ABBYY");
    }

    @Test
    void ocr() {
        mockServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(KrameriusMessengerStreamsResponse.OCR_RESPONSE)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.DEFAULT_BINARY));

        String ocr = krameriusMessenger.getOcrRawStream("test");

        assertThat(ocr).isEqualTo(KrameriusMessengerStreamsResponse.OCR_RESPONSE);
    }

    @Test
    void mods() {
        mockServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(KrameriusMessengerStreamsResponse.MODS_RESPONSE)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_XML));

        ModsCollectionDefinition modsCollectionDefinition = krameriusMessenger.getMods("test");

        TitleInfoDefinition titleInfoDefinition = (TitleInfoDefinition) modsCollectionDefinition.getMods().get(0).getModsGroup().get(0);

        // assertThat call for title
        assertThat(((StringPlusLanguage) ((JAXBElement) titleInfoDefinition.getTitleOrSubTitleOrPartNumber()
                .get(0)).getValue()).getValue()).isEqualTo("Ve škole duchovní");

    }

    private void testPublication(Publication publication, String expectedTitle, String expectedRootTitle,
                                 String expectedRootId, String expectedPolicy, boolean expectedPdf) {
        assertThat(publication.getTitle()).isEqualTo(expectedTitle);
        assertThat(publication.getRootTitle()).isEqualTo(expectedRootTitle);
        assertThat(publication.getPolicy()).isEqualTo(expectedPolicy);
        assertThat(publication.getRootId()).isEqualTo(expectedRootId);
        assertThat(publication.isPdf()).isEqualTo(expectedPdf);
    }

    private DigitalObject testAndGetDigitalObject(String digitalObjectResponse, Class<?> expectedClass, String expectedId) {
        mockServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(digitalObjectResponse)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON));

        DigitalObject digitalObject = krameriusMessenger.getDigitalObject("test");

        assertThat(digitalObject.getClass()).isEqualTo(expectedClass);
        assertThat(digitalObject.getId()).isEqualTo(expectedId);

        return digitalObject;
    }

    private List<DigitalObject> testAndGetChildren(String childrenResponse, Class<?> expectedClass, int expectedCount) {
        mockServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(childrenResponse)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON));

        List<DigitalObject> digitalObjects = krameriusMessenger.getDigitalObjectsForParent("test");

        assertThat(digitalObjects.size()).isEqualTo(expectedCount);
        assertThat(digitalObjects.stream().allMatch(object -> object.getClass().equals(expectedClass))).isTrue();

        return digitalObjects;
    }
}

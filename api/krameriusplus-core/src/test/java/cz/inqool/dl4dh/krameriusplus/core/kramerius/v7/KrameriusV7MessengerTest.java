package cz.inqool.dl4dh.krameriusplus.core.kramerius.v7;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.CoreBaseTest;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.InternalPart;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.periodical.Periodical;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.periodical.PeriodicalItem;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.periodical.PeriodicalVolume;
import cz.inqool.dl4dh.krameriusplus.core.kramerius.MessengerTestHelper;
import cz.inqool.dl4dh.mods.IdentifierDefinition;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import okhttp3.mockwebserver.MockResponse;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.core.kramerius.MessengerTestHelper.testPublication;
import static cz.inqool.dl4dh.krameriusplus.core.kramerius.v7.KrameriusMessengerResponse.*;
import static cz.inqool.dl4dh.krameriusplus.core.kramerius.v7.KrameriusMessengerStreamsResponse.*;
import static cz.inqool.dl4dh.krameriusplus.core.kramerius.v7.KrameriusMessengerStructureResponse.*;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {"system.kramerius.version = 7"})
public class KrameriusV7MessengerTest extends CoreBaseTest {

    @Autowired
    private KrameriusV7Messenger krameriusMessenger;

    @Autowired
    private MessengerTestHelper helper;


    @Test
    void periodical() {
        DigitalObject digitalObject = helper.testAndGetDigitalObject(PERIODICAL_RESPONSE,
                Periodical.class,
                "uuid:2536b1e0-d1c9-11e3-b110-005056827e51",
                krameriusMessenger);
        Periodical periodical = (Periodical) digitalObject;

        testPublication(periodical, "Sborník k dějinám 19. a 20. století",
                "Sborník k dějinám 19. a 20. století",
                "uuid:2536b1e0-d1c9-11e3-b110-005056827e51", "public", false);
    }

    @Test
    void periodicalChildren() {
        List<DigitalObject> digitalObjects = helper.testAndGetChildrenMultipleResponses(List.of(PERIODICAL_CHILDREN_STRUCTURE,
                KrameriusMessengerResponse.PERIODICAL_CHILDREN_CHILD1),
                PeriodicalVolume.class,
                1,
                krameriusMessenger);

        List<PeriodicalVolume> periodicalVolumes = digitalObjects.stream().map(digitalObject -> ((PeriodicalVolume) digitalObject)).collect(Collectors.toList());

        periodicalVolumes.forEach(periodicalVolume ->
                testPublication(periodicalVolume, "1989", "Sborník k dějinám 19. a 20. století",
                        "uuid:2536b1e0-d1c9-11e3-b110-005056827e51", "public", false));

        assertThat(periodicalVolumes.get(0).getVolumeYear()).isEqualTo("1989");
    }

    @Test
    void periodicalVolume() {
        DigitalObject digitalObject = helper.testAndGetDigitalObject(KrameriusMessengerResponse.PERIODICAL_CHILDREN_CHILD1,
                PeriodicalVolume.class,
                "uuid:52432290-d1c9-11e3-94ef-5ef3fc9ae867",
                krameriusMessenger);

        PeriodicalVolume periodicalVolume = (PeriodicalVolume) digitalObject;

        testPublication(periodicalVolume, "1989", "Sborník k dějinám 19. a 20. století",
                "uuid:2536b1e0-d1c9-11e3-b110-005056827e51", "public", false);

        assertThat(periodicalVolume.getVolumeNumber()).isEqualTo("1989");
    }

    @Test
    void periodicalVolumeChildren() {
        List<DigitalObject> digitalObjects = helper.testAndGetChildrenMultipleResponses(List.of(PERIODICAL_VOLUME_CHILDREN_STRUCTURE,
                PERIODICAL_ITEM_RESPONSE), PeriodicalItem.class, 1, krameriusMessenger);

        PeriodicalItem periodicalItem = (PeriodicalItem) digitalObjects.get(0);
        testPublication(periodicalItem, "Sborník k dějinám 19. a 20. století. 11", "Sborník k dějinám 19. a 20. století",
                "uuid:2536b1e0-d1c9-11e3-b110-005056827e51", "public", false);

        assertThat(periodicalItem.getDate()).isEqualTo("1989");
        assertThat(periodicalItem.getPartNumber()).isEqualTo("11");
    }

    @Test
    void periodicalItem() {
        DigitalObject digitalObject = helper.testAndGetDigitalObject(PERIODICAL_ITEM_RESPONSE,
                PeriodicalItem.class,
                "uuid:51202db0-adeb-11e3-9d7d-005056827e51",
                krameriusMessenger);

        PeriodicalItem periodicalItem = (PeriodicalItem) digitalObject;
        testPublication(periodicalItem, "Sborník k dějinám 19. a 20. století. 11", "Sborník k dějinám 19. a 20. století",
                "uuid:2536b1e0-d1c9-11e3-b110-005056827e51", "public", false);

        assertThat(periodicalItem.getDate()).isEqualTo("1989");
        assertThat(periodicalItem.getPartNumber()).isEqualTo("11");
    }

    @Test
    void periodicalItemChildren() {
        List<DigitalObject> digitalObjects = helper.testAndGetChildrenMultipleResponses(List.of(PERIODIAL_ITEM_CHILDREN_STRUCTURE,
                        PERIODICAL_ITEM_INTERNAL_PART1, PERIODICAL_ITEM_INTERNAL_PART2),
                InternalPart.class,
                2,
                krameriusMessenger);

        InternalPart part = ((InternalPart) digitalObjects.get(0));
        InternalPart part1 = ((InternalPart) digitalObjects.get(1));

        assertThat(part.getId()).isEqualTo("uuid:aad65b8b-de92-4267-a529-946940f9d645");
        assertThat(part1.getId()).isEqualTo("uuid:781144b0-f273-40f7-884d-cda9d24d9a3d");
        assertThat(part.getTitle()).isEqualTo("Obálka");
        assertThat(part1.getTitle()).isEqualTo("Vnitřní obálka");
        assertThat(part.getRootId()).isEqualTo("uuid:30985811-c9fe-4798-80c5-2bbdc9f3131a");
        assertThat(part1.getRootId()).isEqualTo("uuid:30985811-c9fe-4798-80c5-2bbdc9f3131a");
        assertThat(part.getRootTitle()).isEqualTo("Jemná mechanika a optika");
        assertThat(part1.getRootTitle()).isEqualTo("Jemná mechanika a optika");
    }

    @Test
    void monograph() {
        DigitalObject digitalObject = helper.testAndGetDigitalObject(MONOGRAPH_RESPONSE,
                Monograph.class,
                "uuid:97a8ba80-d9a8-11e7-bbbb-005056827e51",
                krameriusMessenger);
        Monograph monograph = (Monograph) digitalObject;
        testPublication(monograph, "Statistické obrazy verše",
                "Statistické obrazy verše",
                "uuid:97a8ba80-d9a8-11e7-bbbb-005056827e51", "public", false);
    }

    @Test
    void monographChildren() {
        List<DigitalObject> digitalObjects = helper.testAndGetChildrenMultipleResponses(List.of(MONOGRAPH_CHILDREN_STRUCTURE, MONOGRAPH_PAGE_RESPONSE),
                Page.class,
                1,
                krameriusMessenger);

        Page page = (Page) digitalObjects.get(0);
        assertThat(page.getRootId()).isEqualTo("uuid:97a8ba80-d9a8-11e7-bbbb-005056827e51");
        assertThat(page.getPageNumber()).isEqualTo("[1b]");
        assertThat(page.getPageType()).isEqualTo("frontEndSheet");
        assertThat(page.getTitle()).isEqualTo("[1b]");
    }

    @Test
    void monographUnit() {
        MonographUnit monographUnit = (MonographUnit) helper.testAndGetDigitalObject(MONOGRAPH_UNIT_RESPONSE, MonographUnit.class,
                "uuid:390fa76e-a2f0-40e1-b96f-85785f6b5c05", krameriusMessenger);

        testPublication(monographUnit, "Rudolfu Havlovi: sborník k jeho 70. narozeninám. 3", "Rudolfu Havlovi: sborník k jeho 70. narozeninám",
                "uuid:02df6d4d-e420-4ca2-9fb6-b33c3305a641", "public", false);
        assertThat(monographUnit.getPartNumber()).isEqualTo("3");
    }

    @Test
    void monographUnitChildren() {
        helper.testAndGetChildrenMultipleResponses(List.of(MONOGRAPH_UNIT_CHILDREN, MONOGRAPH_UNIT_PAGE1,
                        MONOGRAPH_UNIT_PAGE2, MONOGRAPH_UNIT_PAGE3),
                Page.class,
                3,
                krameriusMessenger);
    }

    @Test
    void alto() {
        helper.getMockServer().enqueue(new MockResponse().setResponseCode(200)
                .setBody(ALTO_RESPONSE)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_XML));

        Alto alto = krameriusMessenger.getAlto("test");

        assertThat(alto.getDescription().getMeasurementUnit()).isEqualTo("pixel");
        assertThat(alto.getDescription().getOCRProcessing().get(0).getOcrProcessingStep()
                .getProcessingSoftware().getSoftwareCreator()).isEqualTo("Project PERO");
    }

    @Test
    void ocr() {
        helper.getMockServer().enqueue(new MockResponse().setResponseCode(200)
                .setBody(TEXT_RESPONSE)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.DEFAULT_BINARY));

        String ocr = krameriusMessenger.getOcrRawStream("test");

        assertThat(ocr).isEqualTo(TEXT_RESPONSE);
    }

    @Test
    void mods() {
        helper.getMockServer().enqueue(new MockResponse().setResponseCode(200)
                .setBody(MODS)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_XML));

        ModsCollectionDefinition modsCollectionDefinition = krameriusMessenger.getMods("test");

        IdentifierDefinition identifier = (IdentifierDefinition) modsCollectionDefinition.getMods().get(0).getModsGroup().get(1);

        assertThat(identifier.getValue()).isEqualTo("14fd6cc7-79e9-4280-8b3d-82b2b50dc2c1");
    }
}


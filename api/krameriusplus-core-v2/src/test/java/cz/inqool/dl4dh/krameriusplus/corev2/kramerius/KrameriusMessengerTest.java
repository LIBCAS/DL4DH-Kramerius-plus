package cz.inqool.dl4dh.krameriusplus.corev2.kramerius;

import cz.inqool.dl4dh.krameriusplus.corev2.CoreBaseTest;
import cz.inqool.dl4dh.krameriusplus.corev2.TestApplication;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.periodical.Periodical;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.periodical.PeriodicalItem;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.periodical.PeriodicalVolume;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.corev2.config.WebClientConfig.KRAMERIUS_WEB_CLIENT;
import static cz.inqool.dl4dh.krameriusplus.corev2.kramerius.KrameriusMessengerChildrenResponse.*;
import static cz.inqool.dl4dh.krameriusplus.corev2.kramerius.KrameriusMessengerResponse.*;
import static cz.inqool.dl4dh.krameriusplus.corev2.kramerius.KrameriusMessengerStreamsResponse.*;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {
        TestApplication.class,
        KrameriusMessengerTest.TestConfig.class
})
public class KrameriusMessengerTest extends CoreBaseTest {

    public static MockWebServer mockServer;

    @Autowired
    private KrameriusMessenger krameriusMessenger;

    @BeforeAll
    static void beforeAll() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockServer.shutdown();
    }

    @Test
    void periodical() {
        DigitalObject digitalObject = testAndGetDigitalObject(PERIODICAL_RESPONSE, Periodical.class, "uuid:319546a0-5a42-11eb-b4d1-005056827e51");

        assertThat(((Periodical) digitalObject).getTitle())
                .isEqualTo("Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne");
    }

    @Test
    void periodicalChildren() {
        List<DigitalObject> digitalObjects = testAndGetChildren(PERIODICAL_CHILDREN_RESPONSE, PeriodicalVolume.class, 2);

        assertThat(digitalObjects.get(0).getId()).isEqualTo("uuid:986ca2f0-5aaa-11ed-8756-005056827e51");
        assertThat(digitalObjects.get(1).getId()).isEqualTo("uuid:33b874c0-5a42-11eb-a728-5ef3fc9bb22f");
    }

    @Test
    void periodicalVolume() {
        DigitalObject digitalObject = testAndGetDigitalObject(PERIODICAL_VOLUME_RESPONSE, PeriodicalVolume.class, "uuid:986ca2f0-5aaa-11ed-8756-005056827e51");

        assertThat(((PeriodicalVolume) digitalObject).getTitle()).isEqualTo("");
    }

    @Test
    void periodicalVolumeChildren() {
        List<DigitalObject> digitalObjects = testAndGetChildren(PERIODICAL_VOLUME_CHILDREN_RESPONSE, PeriodicalItem.class, 1);

        assertThat(digitalObjects.get(0).getId()).isEqualTo("uuid:e8ebdd40-4ad3-11ed-9b54-5ef3fc9bb22f");
    }

    @Test
    void periodicalItem() {
        DigitalObject digitalObject = testAndGetDigitalObject(PERIODICAL_ITEM_RESPONSE, PeriodicalItem.class, "uuid:e8ebdd40-4ad3-11ed-9b54-5ef3fc9bb22f");

        assertThat(((PeriodicalItem) digitalObject).getTitle()).isEqualTo("");
    }

    @Test
    void periodicalItemChildren() {
        testAndGetChildren(PERIODICAL_ITEM_CHILDREN_RESPONSE, Page.class, 60);
    }

    @Test
    void monograph() {
        testAndGetDigitalObject(MONOGRAPH_RESPONSE, Monograph.class, "uuid:0af541d0-06c8-11e6-a5b6-005056827e52");
    }

    @Test
    void monographChildren() {
        testAndGetChildren(MONOGRAPH_CHILDREN_RESPONSE, Page.class, 6);
    }

    @Test
    void monographUnit() {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Test
    void monographUnitChildren() {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Test
    void altoLowercase() {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Test
    void altoUppercase() {
        mockServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(ALTO_UPPERCASE_STRING_RESPONSE)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.DEFAULT_TEXT));

        String altoString = krameriusMessenger.getAltoString("test");

        assertThat(altoString).isEqualTo(ALTO_UPPERCASE_STRING_RESPONSE);
    }

    @Test
    void altoString() {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Test
    void ocr() {
        mockServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(OCR_RESPONSE)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.DEFAULT_TEXT));

        String ocr = krameriusMessenger.getOcr("test");

        assertThat(ocr).isEqualTo(OCR_RESPONSE);
    }

    @Test
    void mods() {
        mockServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(MODS_RESPONSE)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_XML));

//        ModsCollectionDefition modsCollectionDefition = krameriusMessenger.getMods("test");
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

    @Configuration
    public static class TestConfig {
        @Bean(name = KRAMERIUS_WEB_CLIENT)
        WebClient webClient() {
            HttpUrl httpUrl = mockServer.url("/");
            return WebClient.create(httpUrl.toString());
        }
    }
}

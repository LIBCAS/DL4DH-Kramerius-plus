package cz.inqool.dl4dh.krameriusplus.corev2.kramerius;

import cz.inqool.dl4dh.krameriusplus.corev2.CoreBaseTest;
import cz.inqool.dl4dh.krameriusplus.corev2.TestApplication;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.DigitalObject;
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
import static cz.inqool.dl4dh.krameriusplus.corev2.kramerius.KrameriusMessengerResponse.*;
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
        mockServer.enqueue(
                new MockResponse().setResponseCode(200)
                        .setBody(PERIODICAL_RESPONSE)
                        .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON));

        DigitalObject digitalObject = krameriusMessenger.getDigitalObject("test");

        assertThat(digitalObject.getClass()).isEqualTo(Periodical.class);
        assertThat(digitalObject.getId()).isEqualTo("uuid:319546a0-5a42-11eb-b4d1-005056827e51");
        assertThat(((Periodical) digitalObject).getTitle())
                .isEqualTo("Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne");
    }

    @Test
    void periodicalChildren() {
        mockServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(PERIODICAL_CHILDREN_RESPONSE)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON));

        List<DigitalObject> digitalObjects = krameriusMessenger.getDigitalObjectsForParent("test");

        assertThat(digitalObjects.size()).isEqualTo(2);
        assertThat(digitalObjects.get(0).getId()).isEqualTo("uuid:986ca2f0-5aaa-11ed-8756-005056827e51");
        assertThat(digitalObjects.get(1).getId()).isEqualTo("uuid:33b874c0-5a42-11eb-a728-5ef3fc9bb22f");
    }

    @Test
    void periodicalVolume() {
        mockServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(PERIODICAL_VOLUME_RESPONSE)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON));

        DigitalObject digitalObject = krameriusMessenger.getDigitalObject("test");

        assertThat(digitalObject.getClass()).isEqualTo(PeriodicalVolume.class);
        assertThat(digitalObject.getId()).isEqualTo("uuid:986ca2f0-5aaa-11ed-8756-005056827e51");
        assertThat(((PeriodicalVolume) digitalObject).getTitle()).isEqualTo("");
    }

    @Test
    void periodicalVolumeChildren() {
        mockServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(PERIODICAL_VOLUME_CHILDREN_RESPONSE)
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON));

        List<DigitalObject> digitalObjects = krameriusMessenger.getDigitalObjectsForParent("test");

        assertThat(digitalObjects.size()).isEqualTo(1);
        assertThat(digitalObjects.get(0).getClass()).isEqualTo(PeriodicalItem.class);
        assertThat(digitalObjects.get(0).getId()).isEqualTo("uuid:e8ebdd40-4ad3-11ed-9b54-5ef3fc9bb22f");
    }

    @Test
    void periodicalItem() {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Test
    void periodicalItemChildren() {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Test
    void monograph() {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Test
    void monographChildren() {
        throw new UnsupportedOperationException("Not Yet Implemented.");
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
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Test
    void altoString() {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Test
    void ocr() {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Test
    void mods() {
        throw new UnsupportedOperationException("Not Yet Implemented.");
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

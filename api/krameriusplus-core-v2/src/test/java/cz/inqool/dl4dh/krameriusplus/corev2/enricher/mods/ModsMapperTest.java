package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsMetadata;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsTitleInfo;
import cz.inqool.dl4dh.krameriusplus.corev2.CoreBaseTest;
import cz.inqool.dl4dh.krameriusplus.corev2.kramerius.KrameriusMessenger;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods.ModsResponse.MONOGRAPH_MODS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ModsMapperTest extends CoreBaseTest {

    private ModsMetadata modsMetadata;

    @Autowired
    private ModsMapper modsMapper;

    @Autowired
    private KrameriusMessenger krameriusMessenger;

    @Autowired
    private MockWebServer mockWebServer;

    @BeforeAll
    void beforeAll() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(MONOGRAPH_MODS)
                .addHeader(CONTENT_TYPE, ContentType.TEXT_XML));

        modsMetadata = modsMapper.map(krameriusMessenger.getMods("test"));
    }

    @Test
    void titleInfo() {
        List<ModsTitleInfo> titles = modsMetadata.getTitleInfos();

        assertThat(titles.size()).isEqualTo(2);
        //TODO tests
    }

    @Test
    void modsName() {
        // TODO
    }

    @Test
    void modsGenre() {
        // TODO
    }

    @Test
    void physicalDescription() {
        // TODO
    }

    @Test
    void originInfo() {
        // TODO
    }

    @Test
    void identifiers() {
        // TODO
    }
}

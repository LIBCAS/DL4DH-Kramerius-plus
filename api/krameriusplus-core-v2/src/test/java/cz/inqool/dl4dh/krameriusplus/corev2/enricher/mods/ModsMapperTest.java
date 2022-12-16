package cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsGenre;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsMetadata;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsName;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsTitleInfo;
import cz.inqool.dl4dh.krameriusplus.corev2.CoreBaseTest;
import cz.inqool.dl4dh.krameriusplus.corev2.kramerius.KrameriusMessenger;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.entity.ContentType;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.corev2.enricher.mods.ModsResponse.MONOGRAPH_MODS;
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

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(titles.size()).isEqualTo(2);

            ModsTitleInfo title = titles.get(0);
            softly.assertThat(title.getTitle()).isEqualTo("Výkonnost open source aplikací");
            softly.assertThat(title.getSubTitle()).isEqualTo("rychlost, přesnost a trocha štěstí");

            title = titles.get(1);
            softly.assertThat(title.getTitle()).isEqualTo("Performance of open source applications. Česky");
        });
    }

    @Test
    void modsName() {
        ModsName modsName = modsMetadata.getName();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(modsName.getType()).isEqualTo("personal");
            softly.assertThat(modsName.getNamePart()).isEqualTo("Armstrong, Tavish");
            softly.assertThat(modsName.getNameIdentifier()).isNull();
        });
    }

    @Test
    void modsGenre() {
        List<ModsGenre> genres = modsMetadata.getGenres();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(genres.size()).isEqualTo(3);

            ModsGenre genre = genres.get(0);
            softly.assertThat(genre.getAuthority()).isEqualTo("rdacontent");
            softly.assertThat(genre.getValue()).isEqualTo("text");

            genre = genres.get(1);
            softly.assertThat(genre.getAuthority()).isEqualTo("czenas");
            softly.assertThat(genre.getValue()).isEqualTo("případové studie");

            genre = genres.get(2);
            softly.assertThat(genre.getAuthority()).isEqualTo("eczenas");
            softly.assertThat(genre.getValue()).isEqualTo("case studies");
        });
    }

    @Test
    void originInfo() {
        // TODO
    }

    @Test
    void languages() {
        // TODO
    }

    @Test
    void physicalDescription() {
        // TODO
    }

    @Test
    void identifiers() {
        // TODO
    }
}

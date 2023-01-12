package cz.inqool.dl4dh.krameriusplus.core.enricher.mods;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.*;
import cz.inqool.dl4dh.krameriusplus.core.CoreBaseTest;
import cz.inqool.dl4dh.krameriusplus.core.kramerius.KrameriusMessenger;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.util.TriConsumer;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
                .setBody(ModsResponse.MONOGRAPH_MODS)
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
            softly.assertThat(modsName.getNameParts().size()).isEqualTo(1);
            softly.assertThat(modsName.getNameParts().get(0).getValue()).isEqualTo("Armstrong, Tavish");
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
        List<ModsOriginInfo> originInfos = modsMetadata.getOriginInfo();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(originInfos.size()).isEqualTo(2);

            ModsOriginInfo originInfo1 = originInfos.get(0);

            softly.assertThat(originInfo1.getDatesIssued().get(0).getEncoding()).isEqualTo("marc");
            softly.assertThat(originInfo1.getDatesIssued().get(0).getValue()).isEqualTo("2016");

            softly.assertThat(originInfo1.getIssuances().get(0)).isEqualTo("monographic");
            softly.assertThat(originInfo1.getPublishers()).isEmpty();

            softly.assertThat(originInfo1.getPlaces().size()).isEqualTo(1);

            ModsPlace modsPlace = originInfo1.getPlaces().get(0);
            softly.assertThat(modsPlace.getValue()).isEqualTo("xr");
            softly.assertThat(modsPlace.getAuthority()).isEqualTo("marccountry");
            softly.assertThat(modsPlace.getType()).isEqualTo("code");

            ModsOriginInfo originInfo2 = originInfos.get(1);
            
            softly.assertThat(originInfo2.getPublishers().get(0)).isEqualTo("CZ.NIC, z.s.p.o.,");

            ModsPlace modsPlace2 = originInfo2.getPlaces().get(0);
            softly.assertThat(modsPlace2.getValue()).isEqualTo("Praha :");
        });

    }

    @Test
    void languages() {
        List<ModsLanguage> languages = modsMetadata.getLanguages();
        
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(languages.size()).isEqualTo(2);

            ModsLanguage modsLanguage = languages.get(0);

            softly.assertThat(modsLanguage.getAuthority()).isEqualTo("iso639-2b");
            softly.assertThat(modsLanguage.getType()).isEqualTo("CODE");
            softly.assertThat(modsLanguage.getValue()).isEqualTo("cze");

            ModsLanguage modsLanguage1 = languages.get(1);
            softly.assertThat(modsLanguage1.getObjectPart()).isEqualTo("translation");
            softly.assertThat(modsLanguage1.getAuthority()).isEqualTo("iso639-2b");
            softly.assertThat(modsLanguage1.getValue()).isEqualTo("eng");
        });
    }

    @Test
    void physicalDescription() {
        SoftAssertions.assertSoftly(softly -> {
            ModsPhysicalDescription physicalDescription = modsMetadata.getPhysicalDescription();

            softly.assertThat(physicalDescription.getExtent()).isEqualTo("264 stran : ilustrace ; 25 cm");
        });
    }

    @Test
    void identifiers() {
        List<ModsGenre> genres = modsMetadata.getGenres();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(genres.size()).isEqualTo(3);

            TriConsumer<ModsGenre, String, String> testingMethod = (modsGenre, authority, value) -> {
                softly.assertThat(modsGenre.getAuthority()).isEqualTo(authority);
                softly.assertThat(modsGenre.getValue()).isEqualTo(value);
            };

            testingMethod.accept(genres.get(0), "rdacontent", "text");
            testingMethod.accept(genres.get(1), "czenas", "případové studie");
            testingMethod.accept(genres.get(2), "eczenas", "case studies");
        });
    }
}

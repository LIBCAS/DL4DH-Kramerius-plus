package cz.inqool.dl4dh.krameriusplus.core.enricher.mods.v5;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.*;
import cz.inqool.dl4dh.krameriusplus.core.CoreBaseTest;
import cz.inqool.dl4dh.krameriusplus.core.enricher.mods.ModsMapper;
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
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {"system.kramerius.version=5"})
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
            softly.assertThat(title.getTitles().size()).isEqualTo(1);
            softly.assertThat(title.getTitles().get(0)).isEqualTo("Výkonnost open source aplikací");
            softly.assertThat(title.getSubTitles().size()).isEqualTo(1);
            softly.assertThat(title.getSubTitles().get(0)).isEqualTo("rychlost, přesnost a trocha štěstí");

            title = titles.get(1);
            softly.assertThat(title.getTitles().size()).isEqualTo(1);
            softly.assertThat(title.getTitles().get(0)).isEqualTo("Performance of open source applications. Česky");
            softly.assertThat(title.getType()).isEqualTo("uniform");
        });
    }

    @Test
    void modsName() {
        List<ModsName> modsNames = modsMetadata.getNames();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(modsNames.size()).isEqualTo(1);

            ModsName modsName = modsNames.get(0);

            softly.assertThat(modsName.getType()).isEqualTo("personal");
            softly.assertThat(modsName.getNameParts().size()).isEqualTo(1);
            softly.assertThat(modsName.getNameParts().get(0).getValue()).isEqualTo("Armstrong, Tavish");
            softly.assertThat(modsName.getNameIdentifier()).isEmpty();
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
        List<ModsOriginInfo> originInfos = modsMetadata.getOriginInfos();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(originInfos.size()).isEqualTo(2);

            ModsOriginInfo originInfo = originInfos.get(0);

            softly.assertThat(originInfo.getDatesIssued().size()).isEqualTo(1);
            softly.assertThat(originInfo.getDatesIssued().get(0).getEncoding()).isEqualTo("marc");
            softly.assertThat(originInfo.getDatesIssued().get(0).getValue()).isEqualTo("2016");

            softly.assertThat(originInfo.getIssuances().size()).isEqualTo(1);
            softly.assertThat(originInfo.getIssuances().get(0)).isEqualTo("monographic");
            softly.assertThat(originInfo.getPublishers()).isEmpty();

            softly.assertThat(originInfo.getPlaces().size()).isEqualTo(1);

            ModsPlace modsPlace = originInfo.getPlaces().get(0);
            softly.assertThat(modsPlace.getPlaceTerms().size()).isEqualTo(1);

            ModsPlaceTerm modsPlaceTerm = modsPlace.getPlaceTerms().get(0);

            softly.assertThat(modsPlaceTerm.getValue()).isEqualTo("xr");
            softly.assertThat(modsPlaceTerm.getAuthority()).isEqualTo("marccountry");
            softly.assertThat(modsPlaceTerm.getType()).isEqualTo("code");

            originInfo = originInfos.get(1);
            
            softly.assertThat(originInfo.getPublishers().get(0)).isEqualTo("CZ.NIC, z.s.p.o.,");

            modsPlace = originInfo.getPlaces().get(0);
            softly.assertThat(modsPlace.getPlaceTerms().size()).isEqualTo(1);

            modsPlaceTerm = modsPlace.getPlaceTerms().get(0);

            softly.assertThat(modsPlaceTerm.getValue()).isEqualTo("Praha :");
        });

    }

    @Test
    void languages() {
        List<ModsLanguage> languages = modsMetadata.getLanguages();
        
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(languages.size()).isEqualTo(2);

            ModsLanguage modsLanguage = languages.get(0);
            softly.assertThat(modsLanguage.getObjectPart()).isNull();
            softly.assertThat(modsLanguage.getLanguageTerms().size()).isEqualTo(1);

            ModsLanguageTerm modsLanguageTerm = modsLanguage.getLanguageTerms().get(0);

            softly.assertThat(modsLanguageTerm.getAuthority()).isEqualTo("iso639-2b");
            softly.assertThat(modsLanguageTerm.getType()).isEqualTo("code");
            softly.assertThat(modsLanguageTerm.getValue()).isEqualTo("cze");

            modsLanguage = languages.get(1);
            softly.assertThat(modsLanguage.getObjectPart()).isEqualTo("translation");
            softly.assertThat(modsLanguage.getLanguageTerms().size()).isEqualTo(1);

            modsLanguageTerm = modsLanguage.getLanguageTerms().get(0);

            softly.assertThat(modsLanguageTerm.getType()).isEqualTo("code");
            softly.assertThat(modsLanguageTerm.getAuthority()).isEqualTo("iso639-2b");
            softly.assertThat(modsLanguageTerm.getValue()).isEqualTo("eng");
        });
    }

    @Test
    void physicalDescription() {
        SoftAssertions.assertSoftly(softly -> {
            List<ModsPhysicalDescription> physicalDescriptions = modsMetadata.getPhysicalDescriptions();
            softly.assertThat(physicalDescriptions.size()).isEqualTo(1);

            ModsPhysicalDescription physicalDescription = physicalDescriptions.get(0);

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

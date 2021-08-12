package cz.inqool.dl4dh.krameriusplus.service;

import cz.inqool.dl4dh.krameriusplus.EnricherApplicationContext;
import cz.inqool.dl4dh.krameriusplus.domain.entity.ModsMetadata;
import cz.inqool.dl4dh.krameriusplus.metadata.ModsAdapter;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.StreamProvider;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EnricherApplicationContext.class)
public class ModsUtilsTest {

    @Autowired
    private StreamProvider streamProvider;

    @Test
    public void testExtractModsMetadata() {
        String publicationId = "uuid:319546a0-5a42-11eb-b4d1-005056827e51";

        ModsCollectionDefinition mods = streamProvider.getMods(publicationId);

        assertNotNull(mods);

        ModsAdapter modsAdapter = new ModsAdapter(mods);

        ModsMetadata modsMetadata = modsAdapter.getTransformedMods();

        SoftAssertions.assertSoftly(softAssertions -> {
            // assert TitleInfos
            softAssertions.assertThat(modsMetadata.getTitleInfos().size()).isEqualTo(2);
            softAssertions.assertThat(modsMetadata.getTitleInfos().get(0).getTitle().size()).isEqualTo(1);
            softAssertions.assertThat(modsMetadata.getTitleInfos().get(0).getTitle().get(0)).isEqualTo("Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne");

            softAssertions.assertThat(modsMetadata.getTitleInfos().get(1).getTitle().size()).isEqualTo(1);
            softAssertions.assertThat(modsMetadata.getTitleInfos().get(1).getTitle().get(0)).isEqualTo("Stenografický protokol ... výročního valného shromáždění bratrstva sv. Michala pro království České konaného v Praze dne");
            softAssertions.assertThat(modsMetadata.getTitleInfos().get(1).getType()).isEqualTo("alternative");
            softAssertions.assertThat(modsMetadata.getTitleInfos().get(1).getDisplayLabel()).isEqualTo("Název od r. 1889?:");

            // assert Name
            ModsMetadata.Name name = modsMetadata.getName();
            softAssertions.assertThat(name.getType()).isEqualTo("corporate");
            softAssertions.assertThat(name.getNamePart()).isEqualTo("Bratrstvo sv. Michala");
            softAssertions.assertThat(name.getNameIdentifier()).isEqualTo("kn20050602019");

            // assert PhysicalDescription
            ModsMetadata.PhysicalDescription physicalDescription = modsMetadata.getPhysicalDescription();
            softAssertions.assertThat(physicalDescription.getExtent()).isEqualTo("^^^sv. ; 23 cm");

            // assert OriginInfo
            List<ModsMetadata.OriginInfo.Place> expectedPlaces = new ArrayList<>();
            expectedPlaces.add(new ModsMetadata.OriginInfo.Place("marccountry", "code", "xr"));
            expectedPlaces.add(new ModsMetadata.OriginInfo.Place(null, "text", "V Praze"));

            List<ModsMetadata.OriginInfo.DateIssued> expectedDatesIssued = new ArrayList<>();
            expectedDatesIssued.add(new ModsMetadata.OriginInfo.DateIssued(null, null, "[1878]-[1918]"));
            expectedDatesIssued.add(new ModsMetadata.OriginInfo.DateIssued("marc", "start", "1878"));
            expectedDatesIssued.add(new ModsMetadata.OriginInfo.DateIssued("marc", "end", "1918"));

            ModsMetadata.OriginInfo originInfo = modsMetadata.getOriginInfo();
            softAssertions.assertThat(originInfo.getPublisher()).isEqualTo("Bratrstvo sv. Michala");
            softAssertions.assertThat(originInfo.getPlaces()).isEqualTo(expectedPlaces);
            softAssertions.assertThat(originInfo.getDateIssued()).isEqualTo(expectedDatesIssued);

            // assert Identifiers
            List<ModsMetadata.Identifier> expectedIdentifiers = new ArrayList<>();
            expectedIdentifiers.add(new ModsMetadata.Identifier("barCode", Boolean.FALSE, "1004407716"));
            expectedIdentifiers.add(new ModsMetadata.Identifier("uuid", Boolean.FALSE, "319546a0-5a42-11eb-b4d1-005056827e51"));
            expectedIdentifiers.add(new ModsMetadata.Identifier("ccnb", Boolean.TRUE, "cnb000964752"));
            expectedIdentifiers.add(new ModsMetadata.Identifier("ccnb", Boolean.TRUE, "cnb001503907"));
            expectedIdentifiers.add(new ModsMetadata.Identifier("oclc", Boolean.FALSE, "85591009"));
            expectedIdentifiers.add(new ModsMetadata.Identifier("oclc", Boolean.FALSE, "85001648"));

            softAssertions.assertThat(modsMetadata.getIdentifiers()).isEqualTo(expectedIdentifiers);
        });
    }
}

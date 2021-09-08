package cz.inqool.dl4dh.krameriusplus.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.EnricherApplicationContext;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.NamedEntity;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.enums.NamedEntityType;
import cz.inqool.dl4dh.krameriusplus.dto.LindatServiceResponse;
import cz.inqool.dl4dh.krameriusplus.service.enricher.NameTagService;
import cz.inqool.dl4dh.krameriusplus.service.enricher.UDPipeService;
import lombok.SneakyThrows;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static cz.inqool.dl4dh.krameriusplus.domain.enums.NamedEntityType.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Norbert Bodnar
 */
@SpringBootTest(classes = EnricherApplicationContext.class)
public class NameTagServiceTest {

    @Autowired
    private UDPipeService udPipeService;

    @Autowired
    private NameTagService nameTagService;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    public void testNamedEntityExtraction() {
        Page page = preparePage();

        nameTagService.processTokens(page);

        assertNotNull(page.getNameTagMetadata());

        Map<NamedEntityType, List<NamedEntity>> namedEntities = page.getNameTagMetadata().getNamedEntities();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(namedEntities.size()).isEqualTo(4);

            softAssertions.assertThat(namedEntities.get(INSTITUTIONS).size()).isEqualTo(1);
            softAssertions.assertThat(namedEntities.get(INSTITUTIONS).get(0).getTokens().size()).isEqualTo(5);

            softAssertions.assertThat(namedEntities.get(PERSONAL_NAMES).size()).isEqualTo(2);
            softAssertions.assertThat(namedEntities.get(PERSONAL_NAMES).get(0).getTokens().size()).isEqualTo(1);

            Optional<NamedEntity> pfNamedEntityOptional = namedEntities.get(PERSONAL_NAMES).stream().filter(ne -> ne.getEntityType().equals("pf")).findFirst();

            softAssertions.assertThat(pfNamedEntityOptional.isPresent()).isTrue();

            NamedEntity pfNamedEntity = pfNamedEntityOptional.get();
            softAssertions.assertThat(pfNamedEntity.getEntityType()).isEqualTo("pf");
            softAssertions.assertThat(pfNamedEntity.getTokens().size()).isEqualTo(1);
            softAssertions.assertThat(pfNamedEntity.getTokens().get(0).getLinguisticMetadata().getLemma()).isEqualTo("Václav");

            Optional<NamedEntity> psNamedEntityOptional = namedEntities.get(PERSONAL_NAMES).stream().filter(ne -> ne.getEntityType().equals("ps")).findFirst();

            softAssertions.assertThat(psNamedEntityOptional.isPresent()).isTrue();

            NamedEntity psNamedEntity = psNamedEntityOptional.get();

            softAssertions.assertThat(psNamedEntity.getEntityType()).isEqualTo("ps");
            softAssertions.assertThat(psNamedEntity.getTokens().size()).isEqualTo(1);
            softAssertions.assertThat(psNamedEntity.getTokens().get(0).getLinguisticMetadata().getLemma()).isEqualTo("Havel");

            softAssertions.assertThat(namedEntities.get(GEOGRAPHICAL_NAMES).size()).isEqualTo(4);
        });

        System.out.println(objectMapper.writeValueAsString(page));
    }

    private ResponseEntity<Object> getMockedResponse() {
        LindatServiceResponse response = new LindatServiceResponse();
        response.setResult("Mezinárodní\tB-ic\n" +
                "letiště\tI-ic\n" +
                "Václava\tI-ic|B-P|B-pf\n" +
                "Havla\tI-ic|I-P|B-ps\n" +
                "Praha\tI-ic|B-gu\n" +
                "neboli\tO\n" +
                "Praha\tB-gu\n" +
                "/\tO\n" +
                "Ruzyně\tB-gu\n" +
                "je\tO\n" +
                "veřejné\tO\n" +
                "mezinárodní\tO\n" +
                "letiště\tO\n" +
                "umístěné\tO\n" +
                "na\tO\n" +
                "severozápadním\tO\n" +
                "okraji\tO\n" +
                "Prahy\tB-gu\n" +
                ".\tO\n");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private Page preparePage() {
        Page page = new Page();
        udPipeService.createTokens(page, "Mezinárodní letiště Václava Havla Praha neboli Praha/Ruzyně je veřejné mezinárodní letiště umístěné na severozápadním okraji Prahy.");

        return page;
    }
}

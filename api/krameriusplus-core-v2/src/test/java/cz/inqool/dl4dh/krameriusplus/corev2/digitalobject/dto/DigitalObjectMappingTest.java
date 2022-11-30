package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.monograph.MonographCreateDto;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest //TODO: WRITE TESTS FOR OTHER MODELS!
public class DigitalObjectMappingTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void monograph() throws JsonProcessingException {
        String monographString = "{\n" +
                "    \"dnnt-labels\": [\n" +
                "        \"license\"\n" +
                "    ],\n" +
                "    \"datanode\": true,\n" +
                "    \"pdf\": {\n" +
                "        \"url\": \"https://kramerius.mzk.cz/search/img?pid=uuid:6a7a9842-faff-4af7-a502-a1229611e67f&stream=IMG_FULL&action=GETRAW\"\n" +
                "    },\n" +
                "    \"context\": [\n" +
                "        [\n" +
                "            {\n" +
                "                \"pid\": \"uuid:6a7a9842-faff-4af7-a502-a1229611e67f\",\n" +
                "                \"model\": \"monograph\"\n" +
                "            }\n" +
                "        ]\n" +
                "    ],\n" +
                "    \"pid\": \"uuid:6a7a9842-faff-4af7-a502-a1229611e67f\",\n" +
                "    \"model\": \"monograph\",\n" +
                "    \"handle\": {\n" +
                "        \"href\": \"https://kramerius.mzk.cz/search/handle/uuid:6a7a9842-faff-4af7-a502-a1229611e67f\"\n" +
                "    },\n" +
                "    \"title\": \"Výkonnost open source aplikací : rychlost, přesnost a trocha štěstí\",\n" +
                "    \"root_title\": \"Výkonnost open source aplikací : rychlost, přesnost a trocha štěstí\",\n" +
                "    \"root_pid\": \"uuid:6a7a9842-faff-4af7-a502-a1229611e67f\",\n" +
                "    \"policy\": \"public\",\n" +
                "    \"dnnt\": true\n" +
                "}";

        MonographCreateDto monograph = objectMapper.readValue(monographString, MonographCreateDto.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(monograph.getDnntLabels()).isEqualTo(List.of("license"));
            softly.assertThat(monograph.getDataNode()).isEqualTo(true);
            softly.assertThat(monograph.getPdf().getUrl()).isEqualTo("https://kramerius.mzk.cz/search/img?pid=uuid:6a7a9842-faff-4af7-a502-a1229611e67f&stream=IMG_FULL&action=GETRAW");
            softly.assertThat(monograph.getContext().size()).isEqualTo(1);
            softly.assertThat(monograph.getContext().get(0).size()).isEqualTo(1);
            softly.assertThat(monograph.getContext().get(0).get(0).getPid()).isEqualTo("uuid:6a7a9842-faff-4af7-a502-a1229611e67f");
            softly.assertThat(monograph.getContext().get(0).get(0).getModel()).isEqualTo("monograph");
            softly.assertThat(monograph.getPid()).isEqualTo("uuid:6a7a9842-faff-4af7-a502-a1229611e67f");
            softly.assertThat(monograph.getModel()).isEqualTo("monograph");
            softly.assertThat(monograph.getHandle().getHref()).isEqualTo("https://kramerius.mzk.cz/search/handle/uuid:6a7a9842-faff-4af7-a502-a1229611e67f");
            softly.assertThat(monograph.getTitle()).isEqualTo("Výkonnost open source aplikací : rychlost, přesnost a trocha štěstí");
            softly.assertThat(monograph.getRootTitle()).isEqualTo("Výkonnost open source aplikací : rychlost, přesnost a trocha štěstí");
            softly.assertThat(monograph.getRootPid()).isEqualTo("uuid:6a7a9842-faff-4af7-a502-a1229611e67f");
            softly.assertThat(monograph.getPolicy()).isEqualTo("public");
            softly.assertThat(monograph.getDnnt()).isEqualTo(true);
        });
    }
}

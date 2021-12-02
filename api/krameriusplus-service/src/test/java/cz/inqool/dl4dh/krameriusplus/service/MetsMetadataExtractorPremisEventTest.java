package cz.inqool.dl4dh.krameriusplus.service;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.mets.event.MetsPremisEventElement;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class MetsMetadataExtractorPremisEventTest extends MetsMetadataExtractorTest {

    @Test
    public void hasThreePremisEventsWithCorrectIds() {
        Page page = new Page();

        metsExtractor.enrich(metsFile, page);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(page.getMetsMetadata().getPremisEvents().get("EVT_001")).isNotNull();
            softly.assertThat(page.getMetsMetadata().getPremisEvents().get("EVT_002")).isNotNull();
            softly.assertThat(page.getMetsMetadata().getPremisEvents().get("EVT_003")).isNotNull();
            softly.assertThat(page.getMetsMetadata().getPremisEvents().size()).isEqualTo(3);
        });
    }

    @Test
    public void hasCorrectAttributes() {
        Page page = new Page();

        metsExtractor.enrich(metsFile, page);

        MetsPremisEventElement event = page.getMetsMetadata().getPremisEvents().get("EVT_001");

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(event.getEventType()).isEqualTo("capture");
            softly.assertThat(event.getEventDateTime()).isEqualTo("2018-08-22T06:32:21.596Z");
            softly.assertThat(event.getEventDetail()).isEqualTo("capture/digitization");

            softly.assertThat(event.getEventOutcomeInformation().getOutcome()).isEqualTo("successful");

            softly.assertThat(event.getLinkingAgentIdentifier().getIdentifierType()).isEqualTo("ProArc_AgentID");
            softly.assertThat(event.getLinkingAgentIdentifier().getIdentifierValue()).isEqualTo("ProArc");
            softly.assertThat(event.getLinkingAgentIdentifier().getRoles().size()).isEqualTo(1);
            softly.assertThat(event.getLinkingAgentIdentifier().getRoles().get(0)).isEqualTo("software");
        });
    }
}

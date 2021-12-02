package cz.inqool.dl4dh.krameriusplus.service;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.mets.agent.MetsPremisAgentElement;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class MetsMetadataExtractorPremisAgentTest extends MetsMetadataExtractorTest {

    @Test
    public void hasOnePremisAgentWithCorrectId() {
        Page page = new Page();

        metsExtractor.enrich(metsFile, page);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(page.getMetsMetadata().getPremisAgents().get("AGENT_001")).isNotNull();
            softly.assertThat(page.getMetsMetadata().getPremisAgents().size()).isEqualTo(1);
        });
    }

    @Test
    public void hasCorrectAttributes() {
        Page page = new Page();

        metsExtractor.enrich(metsFile, page);

        MetsPremisAgentElement agent = page.getMetsMetadata().getPremisAgents().get("AGENT_001");

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(agent.getAgentIdentifiers().size()).isEqualTo(1);
            softly.assertThat(agent.getAgentIdentifiers().get(0).getIdentifierType()).isEqualTo("ProArc_AgentID");
            softly.assertThat(agent.getAgentIdentifiers().get(0).getIdentifierValue()).isEqualTo("ProArc");

            softly.assertThat(agent.getAgentNames().size()).isEqualTo(1);
            softly.assertThat(agent.getAgentNames().get(0)).isEqualTo("ProArc");

            softly.assertThat(agent.getAgentType()).isEqualTo("software");
            softly.assertThat(agent.getAgentNote()).isNull();
        });
    }
}

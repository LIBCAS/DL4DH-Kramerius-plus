package cz.inqool.dl4dh.krameriusplus.service;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.agent.MetsPremisAgentElement;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MetsMetadataExtractorPremisAgentTest extends MetsMetadataExtractorTest {

    @BeforeEach
    public void setUp() {
        preparePage();
    }

    @Test
    public void hasOnePremisAgentWithCorrectId() {
        metsExtractor.enrich(page);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(page.getMetsMetadata().getPremisAgents().get("AGENT_001")).isNotNull();
            softly.assertThat(page.getMetsMetadata().getPremisAgents().size()).isEqualTo(1);
        });
    }

    @Test
    public void hasCorrectAttributes() {
        metsExtractor.enrich(page);

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

package cz.inqool.dl4dh.krameriusplus.api.publication.page.udpipe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@EqualsAndHashCode
public class LinguisticMetadataDto {

    private Integer position;

    private String lemma;

    @JsonProperty("uPosTag")
    private String uPosTag;

    @JsonProperty("xPosTag")
    private String xPosTag;

    private String feats;

    private String head;

    private String depRel;

    private String misc;
}

package cz.inqool.dl4dh.krameriusplus.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@EqualsAndHashCode
public class LinguisticMetadata {

    @Field("p")
    private int position;

    @Field("l")
    private String lemma;

    @Field("u")
    private String uPosTag;

    @Field("x")
    private String xPosTag;

    @Field("f")
    private String feats;

    @Field("h")
    private String head;

    @Field("d")
    private String depRel;

    @Field("m")
    private String misc;
}

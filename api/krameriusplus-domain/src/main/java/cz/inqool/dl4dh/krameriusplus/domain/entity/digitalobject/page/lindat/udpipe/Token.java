package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.lindat.udpipe;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.AltoTokenMetadata;
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
public class Token {

    /**
     * Index of the token, numbered from zero. Can differ from {@code udPipeMetadata.position}, since
     * UDPipe numbers the tokens starting from 1 and can occasionally insert a nonexistent token into
     * the response
     */
    @Field("ti")
    private Integer tokenIndex;

    /**
     * Original content of the token
     */
    @Field("c")
    private String content;

    /**
     * Start offset of the token on the page, information extracted from udPipeMetadata.misc field
     */
    @Field("so")
    private Integer startOffset;

    /**
     * End offset of the token on the page, information extracted from udPipeMetadata.misc field
     */
    @Field("eo")
    private Integer endOffset;

    /**
     * Object containing metadata from UDPipe response
     */
    @Field("lm")
    private LinguisticMetadata linguisticMetadata;

    /**
     * String representation of metadata from NameTag
     */
    @Field("ntm")
    private String nameTagMetadata;

    /**
     * Metadata about word position on the physical page obtained from ALTO format.
     */
    @Field("am")
    private AltoTokenMetadata altoMetadata;
}

package cz.inqool.dl4dh.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class Token {

    /**
     * Index of the token, numbered from zero. Can differ from {@code udPipeMetadata.position}, since
     * UDPipe numbers the tokens starting from 1 and can occasionally insert a nonexistent token into
     * the response
     */
    private int tokenIndex;

    /**
     * Original content of the token
     */
    private String content;

    /**
     * Start offset of the token on the page, information extracted from udPipeMetadata.misc field
     */
    private int startOffset;

    /**
     * End offset of the token on the page, information extracted from udPipeMetadata.misc field
     */
    private int endOffset;

    /**
     * Object containing metadata from UDPipe response
     */
    private UDPipeMetadata udPipeMetadata;

    /**
     * String representation of metadata from NameTag
     */
    private String nameTagMetadata;
}

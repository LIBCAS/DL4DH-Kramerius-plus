package cz.inqool.dl4dh.krameriusplus.api.publication.page.udpipe;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.AltoTokenMetadata;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@EqualsAndHashCode
public class TokenDto {

    private Integer tokenIndex;

    private String content;

    private Integer startOffset;

    private Integer endOffset;

    private LinguisticMetadata linguisticMetadata;

    private String nameTagMetadata;

    private AltoTokenMetadata altoMetadata;
}

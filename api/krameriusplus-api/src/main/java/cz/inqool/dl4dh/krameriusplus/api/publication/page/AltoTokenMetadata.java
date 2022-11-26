package cz.inqool.dl4dh.krameriusplus.api.publication.page;

import lombok.Getter;
import lombok.Setter;

/**
 * Metadata for words from ALTO format.
 */
@Getter
@Setter
public class AltoTokenMetadata {

    private Float height;

    private Float width;

    private Float vpos;

    private Float hpos;
}

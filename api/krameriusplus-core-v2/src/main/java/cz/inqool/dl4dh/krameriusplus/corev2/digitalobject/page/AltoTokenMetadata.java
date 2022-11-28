package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page;

import lombok.Getter;

/**
 * Metadata for words from ALTO format.
 */
@Getter
public class AltoTokenMetadata {

    private final Float height;
    private final Float width;
    private final Float vpos;
    private final Float hpos;

    /**
     * Constructor
     * @param height    word height
     * @param width     word width
     * @param vpos      vertical position of the word
     * @param hpos      horizontal position of the word
     */
    public AltoTokenMetadata(Float height, Float width, Float vpos, Float hpos) {
        this.height = height;
        this.width = width;
        this.vpos = vpos;
        this.hpos = hpos;
    }

}

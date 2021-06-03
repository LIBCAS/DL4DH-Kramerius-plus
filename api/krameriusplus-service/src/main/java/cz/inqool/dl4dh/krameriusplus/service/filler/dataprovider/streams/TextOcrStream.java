package cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.streams;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Norbert Bodnar
 */
@Getter
public class TextOcrStream implements KrameriusStream {

    private final String textOcr;

    public TextOcrStream(String textOcr) {
        this.textOcr = textOcr;
    }
}

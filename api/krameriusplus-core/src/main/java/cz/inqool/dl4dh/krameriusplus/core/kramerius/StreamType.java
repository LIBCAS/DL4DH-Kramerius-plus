package cz.inqool.dl4dh.krameriusplus.core.kramerius;

import lombok.Getter;

@Getter
public enum StreamType {
    TEXT_OCR("TEXT_OCR"),
    ALTO("ALTO"),
    MODS("BIBLIO_MODS");

    private final String streamId;

    StreamType(String streamId) {
        this.streamId = streamId;
    }
}
package cz.inqool.dl4dh.krameriusplus.core.kramerius.v5;

import lombok.Getter;

@Getter
enum StreamType {
    TEXT_OCR("TEXT_OCR"),
    ALTO("ALTO"),
    MODS("BIBLIO_MODS");

    private final String streamId;

    StreamType(String streamId) {
        this.streamId = streamId;
    }
}
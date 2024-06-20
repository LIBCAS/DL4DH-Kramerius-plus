package cz.inqool.dl4dh.krameriusplus.core.kramerius.v7;

import lombok.Getter;

@Getter
public enum StreamType {
    TEXT_OCR("ocr/text"),
    ALTO("ocr/alto"),
    MODS("metadata/mods");

    private final String streamId;

    StreamType(String streamId) {
        this.streamId = streamId;
    }
}

package cz.inqool.dl4dh.krameriusplus.api.export.params;

import lombok.Getter;

@Getter
public enum UdPipeParam {
    n("n"),
    lemma("lemma"),
    pos("pos"),
    msd("msd"),
    join("join");

    private final String name;

    UdPipeParam(String name) {
        this.name = name;
    }
}

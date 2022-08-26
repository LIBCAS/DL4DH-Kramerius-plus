package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.tei;

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

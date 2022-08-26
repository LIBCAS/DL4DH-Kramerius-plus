package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.tei;

import lombok.Getter;

@Getter
public enum NameTagParam {
    a("a"),
    g("g"),
    i("i"),
    m("m"),
    n("n"),
    o("o"),
    p("p"),
    t("t");

    private final String name;

    NameTagParam (String name) {
        this.name = name;
    }
}

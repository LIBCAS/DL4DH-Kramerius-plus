package cz.inqool.dl4dh.krameriusplus.core.config;

import lombok.Getter;

@Getter
public enum KrameriusInstance {
    NK("https://kramerius5.nkp.cz"),
    MZK("https://kramerius.mzk.cz"),
    AV("https://kramerius.lib.cas.cz"),
    NDK("https://ndk.cz")
    ;

    private final String url;

    KrameriusInstance(String url) {
        this.url = url;
    }
}

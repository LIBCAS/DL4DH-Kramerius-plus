package cz.inqool.dl4dh.krameriusplus.domain.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Norbert Bodnar
 */
public enum KrameriusModel {
    @JsonProperty("monograph")
    MONOGRAPH,
    @JsonProperty("monographunit")
    MONOGRAPH_UNIT,
    @JsonProperty("periodical")
    PERIODICAL,
    @JsonProperty("page")
    PAGE,
    @JsonProperty("internalpart")
    INTERNAL_PART
}

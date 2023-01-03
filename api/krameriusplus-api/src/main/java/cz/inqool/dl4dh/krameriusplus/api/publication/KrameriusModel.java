package cz.inqool.dl4dh.krameriusplus.api.publication;

import lombok.Getter;

public enum KrameriusModel {
    MONOGRAPH(KrameriusModelName.MONOGRAPH),
    MONOGRAPH_UNIT(KrameriusModelName.MONOGRAPH_UNIT),
    PERIODICAL(KrameriusModelName.PERIODICAL),
    PERIODICAL_VOLUME(KrameriusModelName.PERIODICAL_VOLUME),
    PERIODICAL_ITEM(KrameriusModelName.PERIODICAL_ITEM),
    INTERNAL_PART(KrameriusModelName.INTERNAL_PART),
    SUPPLEMENT(KrameriusModelName.SUPPLEMENT),
    PAGE(KrameriusModelName.PAGE);

    @Getter
    private final String name;

    KrameriusModel(String name) {
        this.name = name;
    }

    public static KrameriusModel fromString(String name) {
        for (KrameriusModel value : KrameriusModel.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }

        throw new IllegalArgumentException("name: " + name + " not allowed in PublicationModel");
    }

    public static class KrameriusModelName {
        public static final String MONOGRAPH = "monograph";
        public static final String MONOGRAPH_UNIT = "monographunit";
        public static final String PERIODICAL = "periodical";
        public static final String PERIODICAL_VOLUME = "periodicalvolume";
        public static final String PERIODICAL_ITEM = "periodicalitem";
        public static final String INTERNAL_PART = "internalpart";
        public static final String SUPPLEMENT = "supplement";
        public static final String PAGE = "page";
    }
}

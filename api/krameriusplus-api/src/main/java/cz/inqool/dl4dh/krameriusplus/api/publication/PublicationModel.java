package cz.inqool.dl4dh.krameriusplus.api.publication;

public enum PublicationModel {
    MONOGRAPH("monograph"),
    MONOGRAPH_UNIT("monographunit"),
    PERIODICAL("periodical"),
    PERIODICAL_VOLUME("periodicalvolume"),
    PERIODICAL_ITEM("periodicalitem"),
    INTERNAL_PART("internalpart"),
    SUPPLEMENT("supplement");

    private final String name;

    PublicationModel(String name) {
        this.name = name;
    }

    public static PublicationModel fromString(String name) {
        for (PublicationModel value : PublicationModel.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }

        throw new IllegalArgumentException("name: " + name + " not allowed in PublicationModel");
    }
}

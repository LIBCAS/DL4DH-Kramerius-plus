package cz.inqool.dl4dh.krameriusplus.core.kramerius.v7;

class KrameriusMessengerStructureResponse {

    static final String PERIODICAL_CHILDREN_STRUCTURE = "{\n" +
            "\"children\": {\n" +
            "\"own\": [\n" +
            "{\n" +
            "\"pid\": \"uuid:52432290-d1c9-11e3-94ef-5ef3fc9ae867\",\n" +
            "\"relation\": \"hasVolume\"\n" +
            "}\n" +
            "],\n" +
            "\"foster\": []\n" +
            "},\n" +
            "\"model\": \"periodical\",\n" +
            "\"parents\": {\n" +
            "\"foster\": []\n" +
            "}\n" +
            "}";

    static final String PERIODICAL_VOLUME_CHILDREN_STRUCTURE = "{\n" +
            "\"children\": {\n" +
            "\"own\": [\n" +
            "{\n" +
            "\"pid\": \"uuid:51202db0-adeb-11e3-9d7d-005056827e51\",\n" +
            "\"relation\": \"hasItem\"\n" +
            "}\n" +
            "],\n" +
            "\"foster\": []\n" +
            "},\n" +
            "\"model\": \"periodicalvolume\",\n" +
            "\"parents\": {\n" +
            "\"own\": {\n" +
            "\"pid\": \"uuid:2536b1e0-d1c9-11e3-b110-005056827e51\",\n" +
            "\"relation\": \"hasVolume\"\n" +
            "},\n" +
            "\"foster\": []\n" +
            "}\n" +
            "}";
}

package cz.inqool.dl4dh.krameriusplus.corev2.kramerius;

public class KrameriusMessengerResponse {

    public static final String PERIODICAL_RESPONSE = "{\n" +
            "    \"datanode\": false,\n" +
            "    \"context\": [\n" +
            "        [\n" +
            "            {\n" +
            "                \"pid\": \"uuid:319546a0-5a42-11eb-b4d1-005056827e51\",\n" +
            "                \"model\": \"periodical\"\n" +
            "            }\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"pid\": \"uuid:319546a0-5a42-11eb-b4d1-005056827e51\",\n" +
            "    \"model\": \"periodical\",\n" +
            "    \"handle\": {\n" +
            "        \"href\": \"https://kramerius.mzk.cz/search/handle/uuid:319546a0-5a42-11eb-b4d1-005056827e51\"\n" +
            "    },\n" +
            "    \"title\": \"Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne\",\n" +
            "    \"root_title\": \"Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne\",\n" +
            "    \"root_pid\": \"uuid:319546a0-5a42-11eb-b4d1-005056827e51\",\n" +
            "    \"policy\": \"public\"\n" +
            "}";


    public static final String PERIODICAL_CHILDREN_RESPONSE = "[\n" +
            "  {\n" +
            "    \"datanode\": false,\n" +
            "    \"pid\": \"uuid:986ca2f0-5aaa-11ed-8756-005056827e51\",\n" +
            "    \"model\": \"periodicalvolume\",\n" +
            "    \"details\": {\n" +
            "      \"volumeNumber\": \"1882\",\n" +
            "      \"year\": \"1882\"\n" +
            "    },\n" +
            "    \"title\": \"\",\n" +
            "    \"root_title\": \"Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne\",\n" +
            "    \"root_pid\": \"uuid:319546a0-5a42-11eb-b4d1-005056827e51\",\n" +
            "    \"policy\": \"public\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"datanode\": false,\n" +
            "    \"pid\": \"uuid:33b874c0-5a42-11eb-a728-5ef3fc9bb22f\",\n" +
            "    \"model\": \"periodicalvolume\",\n" +
            "    \"details\": {\n" +
            "      \"volumeNumber\": \"1898\",\n" +
            "      \"year\": \"1898\"\n" +
            "    },\n" +
            "    \"title\": \"\",\n" +
            "    \"root_title\": \"Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne\",\n" +
            "    \"root_pid\": \"uuid:319546a0-5a42-11eb-b4d1-005056827e51\",\n" +
            "    \"policy\": \"public\"\n" +
            "  }\n" +
            "]";

    public static final String PERIODICAL_VOLUME_RESPONSE = "{\n" +
            "    \"datanode\": false,\n" +
            "    \"context\": [\n" +
            "        [\n" +
            "            {\n" +
            "                \"pid\": \"uuid:319546a0-5a42-11eb-b4d1-005056827e51\",\n" +
            "                \"model\": \"periodical\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"pid\": \"uuid:986ca2f0-5aaa-11ed-8756-005056827e51\",\n" +
            "                \"model\": \"periodicalvolume\"\n" +
            "            }\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"pid\": \"uuid:986ca2f0-5aaa-11ed-8756-005056827e51\",\n" +
            "    \"model\": \"periodicalvolume\",\n" +
            "    \"handle\": {\n" +
            "        \"href\": \"http://www.ndk.cz/search/handle/uuid:986ca2f0-5aaa-11ed-8756-005056827e51\"\n" +
            "    },\n" +
            "    \"details\": {\n" +
            "        \"volumeNumber\": \"1882\",\n" +
            "        \"year\": \"1882\"\n" +
            "    },\n" +
            "    \"title\": \"\",\n" +
            "    \"root_title\": \"Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne\",\n" +
            "    \"root_pid\": \"uuid:319546a0-5a42-11eb-b4d1-005056827e51\",\n" +
            "    \"policy\": \"public\"\n" +
            "}";

    public static final String PERIODICAL_VOLUME_CHILDREN_RESPONSE = "[\n" +
            "    {\n" +
            "        \"datanode\": false,\n" +
            "        \"pid\": \"uuid:e8ebdd40-4ad3-11ed-9b54-5ef3fc9bb22f\",\n" +
            "        \"model\": \"periodicalitem\",\n" +
            "        \"details\": {\n" +
            "            \"date\": \"1882\",\n" +
            "            \"issueNumber\": \"\",\n" +
            "            \"partNumber\": \"5\"\n" +
            "        },\n" +
            "        \"title\": \"Protokol ... veřejné schůze Bratrstva sv. Michala v Praze dne. 5\",\n" +
            "        \"root_title\": \"Protokol ... veřejné schůze bratrstva sv. Michala v Praze dne\",\n" +
            "        \"root_pid\": \"uuid:319546a0-5a42-11eb-b4d1-005056827e51\",\n" +
            "        \"policy\": \"public\"\n" +
            "    }\n" +
            "]";
}

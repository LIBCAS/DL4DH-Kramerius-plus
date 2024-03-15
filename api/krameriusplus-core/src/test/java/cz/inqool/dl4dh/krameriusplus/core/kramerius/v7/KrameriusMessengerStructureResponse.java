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

    static final String PERIODIAL_ITEM_CHILDREN_STRUCTURE = "{\n" +
            "  \"children\": {\n" +
            "    \"own\": [\n" +
            "      {\n" +
            "        \"pid\": \"uuid:aad65b8b-de92-4267-a529-946940f9d645\",\n" +
            "        \"relation\": \"hasIntCompPart\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"pid\": \"uuid:781144b0-f273-40f7-884d-cda9d24d9a3d\",\n" +
            "        \"relation\": \"hasIntCompPart\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"foster\": []\n" +
            "  },\n" +
            "  \"model\": \"periodicalitem\",\n" +
            "  \"parents\": {\n" +
            "    \"own\": {\n" +
            "      \"pid\": \"uuid:b0cc2d83-98bd-4dd9-a54e-8e828fc528f0\",\n" +
            "      \"relation\": \"hasItem\"\n" +
            "    },\n" +
            "    \"foster\": []\n" +
            "  }\n" +
            "}";

    //TODO page id : uuid:9c407440-537c-11e8-afec-005056827e51 has no response in search why?
    static final String MONOGRAPH_CHILDREN_STRUCTURE = "{\n" +
            "  \"children\": {\n" +
            "    \"own\": [\n" +
            "      {\n" +
            "        \"pid\": \"uuid:9c511610-537c-11e8-afec-005056827e51\",\n" +
            "        \"relation\": \"hasPage\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"foster\": []\n" +
            "  },\n" +
            "  \"model\": \"monograph\",\n" +
            "  \"parents\": {\n" +
            "    \"foster\": []\n" +
            "  }\n" +
            "}";

    static final String MONOGRAPH_UNIT_CHILDREN = "{\n" +
            "  \"children\": {\n" +
            "    \"own\": [\n" +
            "      {\n" +
            "        \"pid\": \"uuid:ba52f536-b729-4291-a082-428319436bbe\",\n" +
            "        \"relation\": \"hasPage\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"pid\": \"uuid:d89b53de-955b-44fa-99e6-42b92fc569a8\",\n" +
            "        \"relation\": \"hasPage\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"pid\": \"uuid:14fd6cc7-79e9-4280-8b3d-82b2b50dc2c1\",\n" +
            "        \"relation\": \"hasPage\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"foster\": []\n" +
            "  },\n" +
            "  \"model\": \"monographunit\",\n" +
            "  \"parents\": {\n" +
            "    \"own\": {\n" +
            "      \"pid\": \"uuid:02df6d4d-e420-4ca2-9fb6-b33c3305a641\",\n" +
            "      \"relation\": \"hasUnit\"\n" +
            "    },\n" +
            "    \"foster\": []\n" +
            "  }\n" +
            "}";

    static final String MONOGRAPH_UNIT_PAGE1 = "{\n" +
            "\"response\": {\n" +
            "\"docs\": [\n" +
            "{\n" +
            "\"root.pid\": \"uuid:02df6d4d-e420-4ca2-9fb6-b33c3305a641\",\n" +
            "\"titles.search\": [\n" +
            "\"[376]\"\n" +
            "],\n" +
            "\"accessibility\": \"public\",\n" +
            "\"page.type\": \"titlePage\",\n" +
            "\"date_range_start.year\": 1981,\n" +
            "\"own_parent.title\": \"Rudolfu Havlovi: sborník k jeho 70. narozeninám. 3\",\n" +
            "\"pid\": \"uuid:ba52f536-b729-4291-a082-428319436bbe\",\n" +
            "\"page.index\": 1,\n" +
            "\"rels_ext_index.sort\": 0,\n" +
            "\"pid_paths\": [\n" +
            "\"uuid:02df6d4d-e420-4ca2-9fb6-b33c3305a641/uuid:390fa76e-a2f0-40e1-b96f-85785f6b5c05/uuid:ba52f536-b729-4291-a082-428319436bbe\"\n" +
            "],\n" +
            "\"root.model\": \"monograph\",\n" +
            "\"modified\": \"2023-11-06T16:45:54.058Z\",\n" +
            "\"model\": \"page\",\n" +
            "\"date.min\": \"1981-01-01T00:00:00.001Z\",\n" +
            "\"id_uuid\": [\n" +
            "\"ba52f536-b729-4291-a082-428319436bbe\"\n" +
            "],\n" +
            "\"has_tiles\": true,\n" +
            "\"title.search\": \"[376]\",\n" +
            "\"root.title.sort\": \"RUDOLFU HAVLOVI SBORNI|K K JEHO 70 NAROZENINA|M\",\n" +
            "\"indexer_version\": 19,\n" +
            "\"root.title\": \"Rudolfu Havlovi: sborník k jeho 70. narozeninám\",\n" +
            "\"indexed\": \"2024-03-12T22:30:24.326Z\",\n" +
            "\"level\": 2,\n" +
            "\"created\": \"2023-11-06T16:28:08.760Z\",\n" +
            "\"own_model_path\": \"monograph/monographunit/page\",\n" +
            "\"languages.facet\": [\n" +
            "\"cze\"\n" +
            "],\n" +
            "\"date.max\": \"1981-12-31T23:59:59.999Z\",\n" +
            "\"title.sort\": \"376\",\n" +
            "\"page.number\": \"[376]\",\n" +
            "\"date_range_end.year\": 1981,\n" +
            "\"_version_\": 1793361313092599800,\n" +
            "\"ds.img_full.mime\": \"image/jpeg\",\n" +
            "\"own_parent.model\": \"monographunit\",\n" +
            "\"date.str\": \"1981\",\n" +
            "\"compositeId\": \"uuid:02df6d4d-e420-4ca2-9fb6-b33c3305a641!uuid:ba52f536-b729-4291-a082-428319436bbe\",\n" +
            "\"own_pid_path\": \"uuid:02df6d4d-e420-4ca2-9fb6-b33c3305a641/uuid:390fa76e-a2f0-40e1-b96f-85785f6b5c05/uuid:ba52f536-b729-4291-a082-428319436bbe\",\n" +
            "\"own_parent.pid\": \"uuid:390fa76e-a2f0-40e1-b96f-85785f6b5c05\"\n" +
            "}\n" +
            "],\n" +
            "\"numFound\": 1,\n" +
            "\"start\": 0,\n" +
            "\"maxScore\": 4.3779364,\n" +
            "\"numFoundExact\": true\n" +
            "},\n" +
            "\"responseHeader\": {\n" +
            "\"zkConnected\": true,\n" +
            "\"QTime\": 27,\n" +
            "\"params\": {\n" +
            "\"q\": \"pid:\\\"uuid:ba52f536-b729-4291-a082-428319436bbe\\\"\",\n" +
            "\"hl.fragsize\": \"20\",\n" +
            "\"wt\": \"json\"\n" +
            "},\n" +
            "\"status\": 0\n" +
            "}\n" +
            "}";

    static final String MONOGRAPH_UNIT_PAGE2 = "{\n" +
            "\"response\": {\n" +
            "\"docs\": [\n" +
            "{\n" +
            "\"root.pid\": \"uuid:02df6d4d-e420-4ca2-9fb6-b33c3305a641\",\n" +
            "\"titles.search\": [\n" +
            "\"[377]\"\n" +
            "],\n" +
            "\"accessibility\": \"public\",\n" +
            "\"page.type\": \"frontEndSheet\",\n" +
            "\"date_range_start.year\": 1981,\n" +
            "\"own_parent.title\": \"Rudolfu Havlovi: sborník k jeho 70. narozeninám. 3\",\n" +
            "\"pid\": \"uuid:d89b53de-955b-44fa-99e6-42b92fc569a8\",\n" +
            "\"page.index\": 2,\n" +
            "\"rels_ext_index.sort\": 1,\n" +
            "\"pid_paths\": [\n" +
            "\"uuid:02df6d4d-e420-4ca2-9fb6-b33c3305a641/uuid:390fa76e-a2f0-40e1-b96f-85785f6b5c05/uuid:d89b53de-955b-44fa-99e6-42b92fc569a8\"\n" +
            "],\n" +
            "\"root.model\": \"monograph\",\n" +
            "\"modified\": \"2023-11-06T16:45:53.827Z\",\n" +
            "\"model\": \"page\",\n" +
            "\"date.min\": \"1981-01-01T00:00:00.001Z\",\n" +
            "\"id_uuid\": [\n" +
            "\"d89b53de-955b-44fa-99e6-42b92fc569a8\"\n" +
            "],\n" +
            "\"has_tiles\": true,\n" +
            "\"title.search\": \"[377]\",\n" +
            "\"root.title.sort\": \"RUDOLFU HAVLOVI SBORNI|K K JEHO 70 NAROZENINA|M\",\n" +
            "\"indexer_version\": 19,\n" +
            "\"root.title\": \"Rudolfu Havlovi: sborník k jeho 70. narozeninám\",\n" +
            "\"indexed\": \"2024-03-12T22:30:24.410Z\",\n" +
            "\"level\": 2,\n" +
            "\"created\": \"2023-11-06T16:28:08.435Z\",\n" +
            "\"own_model_path\": \"monograph/monographunit/page\",\n" +
            "\"languages.facet\": [\n" +
            "\"cze\"\n" +
            "],\n" +
            "\"date.max\": \"1981-12-31T23:59:59.999Z\",\n" +
            "\"title.sort\": \"377\",\n" +
            "\"page.number\": \"[377]\",\n" +
            "\"date_range_end.year\": 1981,\n" +
            "\"_version_\": 1793361313176486000,\n" +
            "\"ds.img_full.mime\": \"image/jpeg\",\n" +
            "\"own_parent.model\": \"monographunit\",\n" +
            "\"date.str\": \"1981\",\n" +
            "\"compositeId\": \"uuid:02df6d4d-e420-4ca2-9fb6-b33c3305a641!uuid:d89b53de-955b-44fa-99e6-42b92fc569a8\",\n" +
            "\"own_pid_path\": \"uuid:02df6d4d-e420-4ca2-9fb6-b33c3305a641/uuid:390fa76e-a2f0-40e1-b96f-85785f6b5c05/uuid:d89b53de-955b-44fa-99e6-42b92fc569a8\",\n" +
            "\"own_parent.pid\": \"uuid:390fa76e-a2f0-40e1-b96f-85785f6b5c05\"\n" +
            "}\n" +
            "],\n" +
            "\"numFound\": 1,\n" +
            "\"start\": 0,\n" +
            "\"maxScore\": 4.3779364,\n" +
            "\"numFoundExact\": true\n" +
            "},\n" +
            "\"responseHeader\": {\n" +
            "\"zkConnected\": true,\n" +
            "\"QTime\": 5,\n" +
            "\"params\": {\n" +
            "\"q\": \"pid:\\\"uuid:d89b53de-955b-44fa-99e6-42b92fc569a8\\\"\",\n" +
            "\"hl.fragsize\": \"20\",\n" +
            "\"wt\": \"json\"\n" +
            "},\n" +
            "\"status\": 0\n" +
            "}\n" +
            "}";

    static final String MONOGRAPH_UNIT_PAGE3 = "{\n" +
            "\"response\": {\n" +
            "\"docs\": [\n" +
            "{\n" +
            "\"root.pid\": \"uuid:02df6d4d-e420-4ca2-9fb6-b33c3305a641\",\n" +
            "\"titles.search\": [\n" +
            "\"377a\"\n" +
            "],\n" +
            "\"accessibility\": \"public\",\n" +
            "\"page.type\": \"illustration\",\n" +
            "\"date_range_start.year\": 1981,\n" +
            "\"own_parent.title\": \"Rudolfu Havlovi: sborník k jeho 70. narozeninám. 3\",\n" +
            "\"pid\": \"uuid:14fd6cc7-79e9-4280-8b3d-82b2b50dc2c1\",\n" +
            "\"page.index\": 3,\n" +
            "\"rels_ext_index.sort\": 2,\n" +
            "\"pid_paths\": [\n" +
            "\"uuid:02df6d4d-e420-4ca2-9fb6-b33c3305a641/uuid:390fa76e-a2f0-40e1-b96f-85785f6b5c05/uuid:14fd6cc7-79e9-4280-8b3d-82b2b50dc2c1\"\n" +
            "],\n" +
            "\"root.model\": \"monograph\",\n" +
            "\"modified\": \"2023-11-06T16:45:54.552Z\",\n" +
            "\"model\": \"page\",\n" +
            "\"date.min\": \"1981-01-01T00:00:00.001Z\",\n" +
            "\"id_uuid\": [\n" +
            "\"14fd6cc7-79e9-4280-8b3d-82b2b50dc2c1\"\n" +
            "],\n" +
            "\"has_tiles\": true,\n" +
            "\"title.search\": \"377a\",\n" +
            "\"root.title.sort\": \"RUDOLFU HAVLOVI SBORNI|K K JEHO 70 NAROZENINA|M\",\n" +
            "\"indexer_version\": 19,\n" +
            "\"root.title\": \"Rudolfu Havlovi: sborník k jeho 70. narozeninám\",\n" +
            "\"indexed\": \"2024-03-12T22:30:24.493Z\",\n" +
            "\"level\": 2,\n" +
            "\"created\": \"2023-11-06T16:28:09.506Z\",\n" +
            "\"own_model_path\": \"monograph/monographunit/page\",\n" +
            "\"languages.facet\": [\n" +
            "\"cze\"\n" +
            "],\n" +
            "\"date.max\": \"1981-12-31T23:59:59.999Z\",\n" +
            "\"title.sort\": \"377A\",\n" +
            "\"page.number\": \"377a\",\n" +
            "\"date_range_end.year\": 1981,\n" +
            "\"_version_\": 1793361313263517700,\n" +
            "\"ds.img_full.mime\": \"image/jpeg\",\n" +
            "\"own_parent.model\": \"monographunit\",\n" +
            "\"date.str\": \"1981\",\n" +
            "\"compositeId\": \"uuid:02df6d4d-e420-4ca2-9fb6-b33c3305a641!uuid:14fd6cc7-79e9-4280-8b3d-82b2b50dc2c1\",\n" +
            "\"own_pid_path\": \"uuid:02df6d4d-e420-4ca2-9fb6-b33c3305a641/uuid:390fa76e-a2f0-40e1-b96f-85785f6b5c05/uuid:14fd6cc7-79e9-4280-8b3d-82b2b50dc2c1\",\n" +
            "\"own_parent.pid\": \"uuid:390fa76e-a2f0-40e1-b96f-85785f6b5c05\"\n" +
            "}\n" +
            "],\n" +
            "\"numFound\": 1,\n" +
            "\"start\": 0,\n" +
            "\"maxScore\": 4.3779364,\n" +
            "\"numFoundExact\": true\n" +
            "},\n" +
            "\"responseHeader\": {\n" +
            "\"zkConnected\": true,\n" +
            "\"QTime\": 8,\n" +
            "\"params\": {\n" +
            "\"q\": \"pid:\\\"uuid:14fd6cc7-79e9-4280-8b3d-82b2b50dc2c1\\\"\",\n" +
            "\"hl.fragsize\": \"20\",\n" +
            "\"wt\": \"json\"\n" +
            "},\n" +
            "\"status\": 0\n" +
            "}\n" +
            "}";
}

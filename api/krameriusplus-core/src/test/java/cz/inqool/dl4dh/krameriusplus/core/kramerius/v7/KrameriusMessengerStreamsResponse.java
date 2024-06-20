package cz.inqool.dl4dh.krameriusplus.core.kramerius.v7;

class KrameriusMessengerStreamsResponse {

    static final String ALTO_RESPONSE = "<alto xmlns=\"http://www.loc.gov/standards/alto/ns-v2#\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "<Description>\n" +
            "<MeasurementUnit>pixel</MeasurementUnit>\n" +
            "<sourceImageInformation>\n" +
            "<fileName>0003.jpg</fileName>\n" +
            "</sourceImageInformation>\n" +
            "<OCRProcessing ID=\"IdOcr\">\n" +
            "<ocrProcessingStep>\n" +
            "<processingDateTime>2023-06-22T08:18:27.981470+00:00</processingDateTime>\n" +
            "<processingSoftware>\n" +
            "<softwareCreator>Project PERO</softwareCreator>\n" +
            "<softwareName>PERO OCR</softwareName>\n" +
            "<softwareVersion>v0.1.0</softwareVersion>\n" +
            "</processingSoftware>\n" +
            "</ocrProcessingStep>\n" +
            "</OCRProcessing>\n" +
            "</Description>\n" +
            "<Layout>\n" +
            "<Page ID=\"id_9c1257c9-89e1-4955-a4ae-357bbb7b5dad\" PHYSICAL_IMG_NR=\"1\" HEIGHT=\"4440\" WIDTH=\"2940\">\n" +
            "<TopMargin HEIGHT=\"3220\" WIDTH=\"2940\" VPOS=\"0\" HPOS=\"0\"/>\n" +
            "<LeftMargin HEIGHT=\"4440\" WIDTH=\"1548\" VPOS=\"0\" HPOS=\"0\"/>\n" +
            "<RightMargin HEIGHT=\"4440\" WIDTH=\"0\" VPOS=\"0\" HPOS=\"2940\"/>\n" +
            "<BottomMargin HEIGHT=\"0\" WIDTH=\"2940\" VPOS=\"4440\" HPOS=\"0\"/>\n" +
            "<PrintSpace HEIGHT=\"1220\" WIDTH=\"1392\" VPOS=\"3220\" HPOS=\"1548\">\n" +
            "<TextBlock ID=\"block_9440b105-904d-473d-a7d8-0be511458a26\" HEIGHT=\"217\" WIDTH=\"412\" VPOS=\"3220\" HPOS=\"1548\">\n" +
            "<TextLine BASELINE=\"3386\" VPOS=\"3220\" HPOS=\"1548\" HEIGHT=\"217\" WIDTH=\"412\">\n" +
            "<String CONTENT=\"Pák4.\" HEIGHT=\"217\" WIDTH=\"420\" VPOS=\"3219\" HPOS=\"1550\" WC=\"0.36\"/>\n" +
            "</TextLine>\n" +
            "</TextBlock>\n" +
            "<TextBlock ID=\"block_118e37c6-1ebf-498e-8dba-c4c66f05de26\" HEIGHT=\"88\" WIDTH=\"268\" VPOS=\"4295\" HPOS=\"2624\">\n" +
            "<TextLine BASELINE=\"4362\" VPOS=\"4295\" HPOS=\"2624\" HEIGHT=\"88\" WIDTH=\"268\">\n" +
            "<String CONTENT=\"323a\" HEIGHT=\"87\" WIDTH=\"213\" VPOS=\"4294\" HPOS=\"2664\" WC=\"0.8\"/>\n" +
            "</TextLine>\n" +
            "</TextBlock>\n" +
            "</PrintSpace>\n" +
            "</Page>\n" +
            "</Layout>\n" +
            "</alto>";

    static final String TEXT_RESPONSE = "Pák4.\n" +
            "323a";

    static final String MODS = "<mods:modsCollection xmlns:mods=\"http://www.loc.gov/mods/v3\" xmlns=\"info:fedora/fedora-system:def/foxml#\" xmlns:foxml=\"info:fedora/fedora-system:def/foxml#\" xmlns:mets=\"http://www.loc.gov/METS/\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "<mods:mods ID=\"MODS_PAGE_0003\">\n" +
            "<mods:genre type=\"illustration\">page</mods:genre>\n" +
            "<mods:identifier type=\"uuid\">14fd6cc7-79e9-4280-8b3d-82b2b50dc2c1</mods:identifier>\n" +
            "<mods:part type=\"illustration\">\n" +
            "<mods:detail type=\"pageNumber\">\n" +
            "<mods:number>377a</mods:number>\n" +
            "</mods:detail>\n" +
            "<mods:extent unit=\"pages\">\n" +
            "<mods:start>377a</mods:start>\n" +
            "</mods:extent>\n" +
            "</mods:part>\n" +
            "<mods:part>\n" +
            "<mods:detail type=\"pageIndex\">\n" +
            "<mods:number>3</mods:number>\n" +
            "</mods:detail>\n" +
            "</mods:part>\n" +
            "<mods:typeOfResource>text</mods:typeOfResource>\n" +
            "</mods:mods>\n" +
            "</mods:modsCollection>";
}

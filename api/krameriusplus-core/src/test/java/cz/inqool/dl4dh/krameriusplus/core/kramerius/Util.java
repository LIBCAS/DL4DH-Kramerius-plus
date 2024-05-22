package cz.inqool.dl4dh.krameriusplus.core.kramerius;

public class Util {

    public static String normalizeText(String text) {
        return text.replace("\uFEFF", "")
                .replaceAll("\\S-\r\n", "")
                .replaceAll("\\S-\n", "")
                .replaceAll("\\S–\r\n", "")
                .replaceAll("\\S–\n", "")
                .replaceAll("\r\n", " ")
                .replaceAll("\n", " ");
    }
}

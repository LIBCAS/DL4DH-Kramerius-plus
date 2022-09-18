package cz.inqool.dl4dh.krameriusplus.core.system.bulkexport;

import lombok.Getter;
import org.apache.http.entity.ContentType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public enum ExportFormat {
    JSON("json", ContentType.APPLICATION_JSON),
    TEI("xml", ContentType.TEXT_XML),
    CSV("csv", ContentType.create("text/csv")),
    ALTO("xml", ContentType.TEXT_XML),
    TEXT("txt", ContentType.TEXT_PLAIN);

    @Getter
    private final String suffix;

    @Getter
    private final ContentType mimeType;

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");

    ExportFormat(String fileSuffix, ContentType mimeType) {
        this.suffix = fileSuffix;
        this.mimeType = mimeType;
    }

    public String getFileName(String publicationId) {
        String idWithoutPrefix = publicationId.substring(publicationId.indexOf(':') + 1);

        return String.format("uuid_%s_%s.%s", idWithoutPrefix, dateTimeFormatter.format(LocalDate.now()), suffix);
    }

    public String getFileName(String prefix, String publicationId) {
        String idWithoutPrefix = publicationId.substring(publicationId.indexOf(':') + 1);

        return String.format("%s_%s.%s", prefix, idWithoutPrefix, suffix);
    }
}

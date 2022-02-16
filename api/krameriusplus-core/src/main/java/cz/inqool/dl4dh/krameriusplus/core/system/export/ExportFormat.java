package cz.inqool.dl4dh.krameriusplus.core.system.export;

import lombok.Getter;
import org.apache.http.entity.ContentType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public enum ExportFormat {
    JSON("json", ContentType.APPLICATION_JSON),
    TEI("xml", ContentType.TEXT_XML),
    CSV("zip", ContentType.create("application/zip")),
    TSV("zip", ContentType.create("application/zip"));

    private final String suffix;

    @Getter
    private final ContentType mimeType;

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");

    ExportFormat(String fileSuffix, ContentType mimeType) {
        this.suffix = fileSuffix;
        this.mimeType = mimeType;
    }

    public static ExportFormat fromString(String value) {
        for (ExportFormat format : ExportFormat.values()) {
            if (format.toString().equalsIgnoreCase(value)) {
                return format;
            }
        }

        throw new IllegalArgumentException("Enum value from value:" + value + " wasn't found");
    }

    public String getFileName(String publicationId) {
        String idWithoutPrefix = publicationId.substring(publicationId.indexOf(':') + 1);

        return String.format("uuid_%s_%s.%s", idWithoutPrefix, dateTimeFormatter.format(LocalDate.now()), suffix);
    }
}

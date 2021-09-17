package cz.inqool.dl4dh.krameriusplus.service.export;

import lombok.Getter;

public enum ExportFormat {
    JSON("json"),
    TEI("xml");

    @Getter
    private final String suffix;

    ExportFormat(String fileSuffix) {
        this.suffix = fileSuffix;
    }

    public static ExportFormat fromString(String value) {
        for (ExportFormat format : ExportFormat.values()) {
            if (format.toString().equalsIgnoreCase(value)) {
                return format;
            }
        }

        throw new IllegalArgumentException("Enum value from value:" + value + " wasn't found");
    }
}

package cz.inqool.dl4dh.krameriusplus.core.jms;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class ExportMessage {

    private Long executionId;

    private String publicationId;

    private String publicationTitle;

    private Params params;

    private ExportFormat exportFormat;

    private Date timestamp;

    public static ExportMessage ofExisting(Long executionId) {
        ExportMessage exportMessage = new ExportMessage();
        exportMessage.executionId = executionId;

        return exportMessage;
    }

    public static ExportMessage newInstance(String publicationId, String publicationTitle, Params params, ExportFormat exportFormat, Date timestamp) {
        ExportMessage exportMessage = new ExportMessage();
        exportMessage.publicationId = publicationId;
        exportMessage.publicationTitle = publicationTitle;
        exportMessage.params = params;
        exportMessage.exportFormat = exportFormat;
        exportMessage.timestamp = timestamp;

        return exportMessage;
    }
}

package cz.inqool.dl4dh.krameriusplus.core.jms;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExportMessage {

    private String publicationId;

    private String publicationTitle;

    private Params params;

    private ExportFormat exportFormat;
}

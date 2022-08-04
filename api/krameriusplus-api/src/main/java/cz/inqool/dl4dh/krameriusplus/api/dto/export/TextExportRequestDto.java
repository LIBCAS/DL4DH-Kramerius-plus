package cz.inqool.dl4dh.krameriusplus.api.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export.TextExportJobConfigDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TextExportRequestDto extends ExportRequestBase {

    @NotNull
    private TextExportJobConfigDto config;

    public TextExportRequestDto(String name, @NotNull String publicationId, TextExportJobConfigDto config) {
        super(name, publicationId);
        this.config = config;
    }
}

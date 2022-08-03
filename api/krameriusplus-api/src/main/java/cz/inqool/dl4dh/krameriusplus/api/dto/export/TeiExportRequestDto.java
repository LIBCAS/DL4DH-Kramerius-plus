package cz.inqool.dl4dh.krameriusplus.api.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export.TeiExportJobConfigDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TeiExportRequestDto extends ExportRequestBase {

    @NotNull
    private TeiExportJobConfigDto config;

    public TeiExportRequestDto(String name, @NotNull String publicationId, TeiExportJobConfigDto config) {
        super(name, publicationId);
        this.config = config;
    }
}

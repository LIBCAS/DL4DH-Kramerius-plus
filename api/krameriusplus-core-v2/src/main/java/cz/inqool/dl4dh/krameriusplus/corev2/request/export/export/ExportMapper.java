package cz.inqool.dl4dh.krameriusplus.corev2.request.export.export;

import cz.inqool.dl4dh.krameriusplus.api.export.ExportDto;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileRefMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExportMapper {

    private FileRefMapper fileRefMapper;

    public ExportDto toDto(Export export) {
        if (export == null) {
            return null;
        }

        ExportDto exportDto = new ExportDto();

        exportDto.setId(export.getId());
        exportDto.setPublicationId(exportDto.getPublicationId());
        exportDto.setPublicationTitle(export.getPublicationTitle());
        exportDto.setExportJob(exportDto.getExportJob());
        exportDto.setFileRef(fileRefMapper.toDto(export.getFileRef()));

        return exportDto;
    }

    public Export fromDto(ExportDto exportDto) {
        if (exportDto == null) {
            return null;
        }

        Export export = new Export();

        export.setId(exportDto.getId());
        export.setPublicationId(export.getPublicationId());
        export.setPublicationTitle(exportDto.getPublicationTitle());
        export.setExportJob(export.getExportJob());
        export.setFileRef(fileRefMapper.fromDto(exportDto.getFileRef()));

        return export;
    }

    @Autowired
    public void setFileRefMapper(FileRefMapper fileRefMapper) {
        this.fileRefMapper = fileRefMapper;
    }
}

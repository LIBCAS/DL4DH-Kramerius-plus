package cz.inqool.dl4dh.krameriusplus.corev2.request.export.bulk;

import cz.inqool.dl4dh.krameriusplus.api.export.BulkExportDto;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileRefMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.ExportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BulkExportMapper {

    private KrameriusJobInstanceMapper jobInstanceMapper;

    private ExportMapper exportMapper;

    private FileRefMapper fileRefMapper;

    public BulkExport fromDto(BulkExportDto dto) {
        if (dto == null) {
            return null;
        }

        BulkExport bulkExport = new BulkExport();

        bulkExport.setId(dto.getId());
        bulkExport.setFormat(dto.getExportFormat());
        bulkExport.setExports(dto.getExportJobs().stream().map(exportMapper::fromDto).collect(Collectors.toList()));
        bulkExport.setFileRef(fileRefMapper.fromDto(dto.getFileRef()));

        return bulkExport;
    }

    public BulkExportDto toDto(BulkExport entity) {
        if (entity == null) {
            return null;
        }

        BulkExportDto bulkExportDto = new BulkExportDto();

        bulkExportDto.setId(entity.getId());
        bulkExportDto.setExportFormat(entity.getFormat());
        bulkExportDto.setExportJobs(entity.getExports().stream().map(exportMapper::toDto).collect(Collectors.toList()));
        bulkExportDto.setFileRef(fileRefMapper.toDto(entity.getFileRef()));
        bulkExportDto.setMergeJob(jobInstanceMapper.toDto(entity.getMergeJob()));

        return bulkExportDto;
    }

    @Autowired
    public void setJobInstanceMapper(KrameriusJobInstanceMapper jobInstanceMapper) {
        this.jobInstanceMapper = jobInstanceMapper;
    }

    @Autowired
    public void setExportMapper(ExportMapper exportMapper) {
        this.exportMapper = exportMapper;
    }

    @Autowired
    public void setFileRefMapper(FileRefMapper fileRefMapper) {
        this.fileRefMapper = fileRefMapper;
    }
}

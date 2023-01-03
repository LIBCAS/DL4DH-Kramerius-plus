package cz.inqool.dl4dh.krameriusplus.core.job.config.export;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.core.job.config.JobParametersMapWrapper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

import static cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey.DELIMITER;

@Getter
@Setter
@Entity
public class ExportCsvJobConfig extends ExportJobConfig {

    private String delimiter = ",";

    @Override
    public JobParametersMapWrapper toJobParametersWrapper() {
        JobParametersMapWrapper jobParametersMapWrapper = super.toJobParametersWrapper();

        jobParametersMapWrapper.putString(DELIMITER, delimiter);

        return jobParametersMapWrapper;
    }

    @Override
    public KrameriusJobType getJobType() {
        return KrameriusJobType.EXPORT_CSV;
    }

    @Override
    public ExportFormat getExportFormat() {
        return ExportFormat.CSV;
    }
}

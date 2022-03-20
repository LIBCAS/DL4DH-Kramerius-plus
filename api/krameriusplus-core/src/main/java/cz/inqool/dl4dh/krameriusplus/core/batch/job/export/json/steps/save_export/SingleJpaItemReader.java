package cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.steps.save_export;

import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.util.HashMap;
import java.util.Map;

public class SingleJpaItemReader extends JpaPagingItemReader<FileRef> {

    @Override
    public FileRef read() throws Exception {
        System.out.println("Reading");
        FileRef fileRef = super.read();

        return fileRef;
    }

    @BeforeStep
    public void retrieveId(StepExecution stepExecution) throws Exception {
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", executionContext.get("fileRefId"));
        setParameterValues(parameters);



        afterPropertiesSet();
    }
}

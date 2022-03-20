package cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json;

import lombok.Getter;

@Getter
public class JsonExportingSteps {

    public static class GenerateFileStep {
        public static final String STEP_NAME = "generateFileStep";
        public static final String READER_NAME = STEP_NAME + "Reader";
        public static final String PROCESSOR_NAME = STEP_NAME + "Processor";
        public static final String WRITER_NAME = STEP_NAME + "Writer";
    }

    public static class SaveExportStep {
        public static final String STEP_NAME = "saveExportStep";
        public static final String READER_NAME = STEP_NAME + "Reader";
        public static final String PROCESSOR_NAME = STEP_NAME + "Processor";
        public static final String WRITER_NAME = STEP_NAME + "Writer";
    }
}

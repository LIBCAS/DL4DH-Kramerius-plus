package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps;

import lombok.Getter;

@Getter
public class EnrichingSteps {

    public static class DownloadPublicationStep {
        public static final String STEP_NAME = "downloadPublication";
        public static final String READER_NAME = STEP_NAME + "Reader";
        public static final String PROCESSOR_NAME = STEP_NAME + "Processor";
    }

    public static class DownloadPagesAltoStep {
        public static final String STEP_NAME = "downloadPagesAlto";
        public static final String PROCESSOR_NAME = STEP_NAME + "Processor";
    }

    public static class EnrichPublicationMods {
        public static final String STEP_NAME = "enrichPublicationMods";
        public static final String PROCESSOR_NAME = STEP_NAME + "Processor";
    }

    public static class EnrichPublicationTei {
        public static final String STEP_NAME = "enrichPublicationTei";
        public static final String PROCESSOR_NAME = STEP_NAME + "Processor";
    }

    public static class PrepareNdkPublicationPath {
        public static final String STEP_NAME = "prepareNdkPublicationPath";
        public static final String PROCESSOR_NAME = STEP_NAME + "Processor";
    }

    public static class PrepareNdkPagesPath {
        public static final String STEP_NAME = "prepareNdkPagesPath";
        public static final String PROCESSOR_NAME = STEP_NAME + "Processor";
    }

    public static class EnrichPagesNdk {
        public static final String STEP_NAME = "enrichPagesNdk";
        public static final String PROCESSOR_NAME = STEP_NAME + "Processor";
    }

    public static class EnrichPagesUDPipe {
        public static final String STEP_NAME = "enrichPagesUDPipe";
        public static final String PROCESSOR_NAME = STEP_NAME + "Processor";
    }

    public static class EnrichPagesNameTag {
        public static final String STEP_NAME = "enrichPagesNameTag";
        public static final String PROCESSOR_NAME = STEP_NAME + "Processor";
    }

    public static class EnrichPagesAlto {
        public static final String STEP_NAME = "enrichPagesAlto";
        public static final String PROCESSOR_NAME = STEP_NAME + "Processor";
    }

    public static class EnrichPagesTei {
        public static final String STEP_NAME = "enrichPagesTei";
        public static final String PROCESSOR_NAME = STEP_NAME + "Processor";
    }

    public static class CleanUpPages {
        public static final String STEP_NAME = "cleanUpPages";
        public static final String PROCESSOR_NAME = STEP_NAME + "Processor";
    }

}

package cz.inqool.dl4dh.krameriusplus.core.job.export.json.steps;

import lombok.Getter;

@Getter
public enum JsonExportingSteps {
    ;

    public static final String WRITE_PUBLICATION_START = "writePublicationStartStep";
    public static final String WRITE_PAGES = "writePagesStep";
    public static final String WRITE_PUBLICATION_END = "writePublicationEndStep";
    public static final String SAVE_FILE = "saveFileStep";
    public static final String CLEAN_UP = "cleanUpStep";
    public static final String SAVE_EXPORT = "saveExportStep";
}

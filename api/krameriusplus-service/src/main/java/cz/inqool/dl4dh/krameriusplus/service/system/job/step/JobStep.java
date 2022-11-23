package cz.inqool.dl4dh.krameriusplus.service.system.job.step;

public class JobStep {
    // ALL COMMON
    public static final String VALIDATE_PREREQUISITES = "ValidatePrerequisitesStep";

    // ENRICHMENT_KRAMERIUS Job
    public static final String DOWNLOAD_DIGITAL_OBJECTS = "DownloadDigitalObjectStep";

    // ENRICHMENT_NDK Job
    public static final String PREPARE_PUBLICATION_NDK = "PreparePublicationNdkDirectoryPathStep";
    public static final String ENRICH_PAGES_NDK = "EnrichPagesNdkStep";
    public static final String ENRICH_PAGES_NDK_MASTER = "EnrichPagesNdkStep-MASTER";

    // ENRICHMENT_TEI Job
    public static final String ENRICH_PUBLICATION_TEI = "EnrichPublicationTeiHeaderStep";
    public static final String ENRICH_PAGES_TEI = "EnrichPagesTeiBodyStep";
    public static final String ENRICH_PAGES_TEI_MASTER = "EnrichPagesTeiBodyStep-MASTER";

    // ENRICH PAGES PHASE
    public static final String ENRICH_PAGES_ALTO = "EnrichPagesAltoStep";
    public static final String ENRICH_PAGES_ALTO_MASTER = "EnrichPagesAltoStep-MASTER";
    public static final String ENRICH_PUBLICATION_MODS = "EnrichPublicationModsStep";

    // EXPORT COMMON
    public static final String PREPARE_PUBLICATION_METADATA = "PreparePublicationMetadataStep";
    public static final String PREPARE_EXPORT_DIRECTORY = "PrepareExportDirectoryStep";
    public static final String ZIP_EXPORT = "ZipExportStep";
    public static final String CREATE_EXPORT = "CreateExportStep";
    public static final String CLEAN_UP_EXPORT = "CleanUpExportStep";
    public static final String UNZIP_EXPORTS = "UnzipExportsStep";
    public static final String CREATE_BULK_FILE_REF = "CreateBulkFileRefStep";
    public static final String CREATE_BULK_EXPORT = "CreateBulkExportStep";

    // EXPORT JSON
    public static final String EXPORT_PUBLICATION_JSON = "ExportPublicationJsonStep";
    public static final String EXPORT_PAGES_JSON = "ExportPagesJsonStep";
    public static final String EXPORT_PAGES_JSON_MASTER = "ExportPagesJsonStep-MASTER";

    // EXPORT CSV
    public static final String EXPORT_PUBLICATION_CSV = "ExportPublicationCsvStep";
    public static final String EXPORT_PAGES_CSV = "ExportPagesCsvStep";
    public static final String EXPORT_PAGES_CSV_MASTER = "ExportPagesCsvStep-MASTER";

    // EXPORT TEI
    public static final String EXPORT_TEI = "ExportTeiStep";

    // EXPORT ALTO
    public static final String EXPORT_PAGES_ALTO = "ExportPagesAltoStep";

    // EXPORT TEXT
    public static final String EXPORT_PAGES_TEXT = "ExportPagesTextStep";
}

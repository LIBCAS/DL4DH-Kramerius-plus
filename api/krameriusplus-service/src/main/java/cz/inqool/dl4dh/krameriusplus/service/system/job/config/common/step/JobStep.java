package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step;

public class JobStep {

    // ENRICHMENT_COMMON
    public static final String ENRICHMENT_VALIDATION = "EnrichmentValidationStep";

    // ENRICHMENT_KRAMERIUS Job
    public static final String DOWNLOAD_PUBLICATION = "DownloadPublicationStep";
    public static final String DOWNLOAD_PUBLICATION_CHILDREN = "DownloadPublicationChildrenStep";

    // ENRICHMENT_NDK Job
    public static final String PREPARE_PUBLICATION_NDK = "PreparePublicationNdkDirectoryPathStep";
    public static final String ENRICH_PUBLICATION_NDK = "EnrichPublicationNdkStep";
    public static final String PREPARE_PAGES_NDK = "PreparePagesNdkFilePathStep";
    public static final String ENRICH_PAGES_NDK = "EnrichPagesNdkStep";

    // ENRICHMENT_TEI Job
    public static final String ENRICH_PUBLICATION_TEI = "EnrichPublicationTeiHeaderStep";
    public static final String ENRICH_PAGES_TEI = "EnrichPagesTeiBodyStep";

    // ENRICH PAGES PHASE
    public static final String DOWNLOAD_PAGES_ALTO = "DownloadPagesAltoStep";
    public static final String ENRICH_PAGES_UD_PIPE = "EnrichPagesUDPipeStep";
    public static final String ENRICH_PAGES_NAME_TAG = "EnrichPagesNameTagStep";
    public static final String ENRICH_PAGES_ALTO = "EnrichPagesAltoStep";
    public static final String CLEAN_UP_PAGES = "CleanUpPagesStep";

    // EXPORT COMMON
    public static final String PREPARE_EXPORT_DIRECTORY = "PrepareExportDirectoryStep";
    public static final String ZIP_EXPORT = "ZipExportStep";
    public static final String CREATE_EXPORT = "CreateExportStep";
    public static final String CLEAN_UP_EXPORT = "CleanUpExportStep";

    // EXPORT JSON
    public static final String EXPORT_PUBLICATION_JSON = "ExportPublicationJsonStep";
    public static final String EXPORT_PAGES_JSON = "ExportPagesJsonStep";

    // EXPORT CSV
    public static final String EXPORT_PUBLICATION_CSV = "ExportPublicationCsvStep";
    public static final String EXPORT_PAGES_CSV = "ExportPagesCsvStep";

    // EXPORT TEI
    public static final String EXPORT_TEI = "ExportTeiStep";

    // EXPORT ALTO
    public static final String EXPORT_PAGES_ALTO = "ExportPagesAltoStep";
}

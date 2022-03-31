package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EnrichingStep {
    ;

    // DOWNLOAD PHASE
    public static final String DOWNLOAD_PUBLICATION = "DownloadPublicationStep";
    public static final String DOWNLOAD_PUBLICATION_CHILDREN = "DownloadPublicationChildrenStep";

    // ENRICH NDK PHASE
    public static final String PREPARE_PUBLICATION_NDK = "PreparePublicationNdkDirectoryPathStep";
    public static final String ENRICH_PUBLICATION_NDK = "EnrichPublicationNdkStep";
    public static final String PREPARE_PAGES_NDK = "PreparePagesNdkFilePathStep";
    public static final String ENRICH_PAGES_NDK = "EnrichPagesNdkStep";

    // ENRICH PUBLICATION PHASE
    public static final String ENRICH_PUBLICATION_TEI = "EnrichPublicationTeiHeaderStep";

    // ENRICH PAGES PHASE
    public static final String DOWNLOAD_PAGES_ALTO = "DownloadPagesAltoStep";
    public static final String ENRICH_PAGES_UD_PIPE = "EnrichPagesUDPipeStep";
    public static final String ENRICH_PAGES_NAME_TAG = "EnrichPagesNameTagStep";
    public static final String ENRICH_PAGES_ALTO = "EnrichPagesAltoStep";
    public static final String ENRICH_PAGES_TEI = "EnrichPagesTeiBodyStep";

    public static final String CLEAN_UP_PAGES = "CleanUpPagesStep";
}
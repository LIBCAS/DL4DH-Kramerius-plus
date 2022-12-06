package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.api.exception.NdkEnrichmentException;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.NdkFilePathWrappedPage;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk.MetsExplorer;
import cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk.MetsFileLocatorService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.api.exception.NdkEnrichmentException.ErrorCode.NDK_PAGE_DIV_NOT_FOUND;
import static cz.inqool.dl4dh.krameriusplus.api.exception.NdkEnrichmentException.ErrorCode.NDK_PAGE_FILE_NOT_FOUND;
import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.isTrue;
import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.notNull;

@Component
@StepScope
public class NdkFilePathFinderProcessor implements ItemProcessor<Page, NdkFilePathWrappedPage> {

    private MetsFileLocatorService metsFileLocatorService;

    private MetsExplorer metsExplorer;

    /**
     * Map from page title to the Path of the corresponding mets file
     */
    private Map<String, Path> pageTitleToPathMap;

    @Override
    public NdkFilePathWrappedPage process(Page item) {
        if (pageTitleToPathMap == null) {
            computePageTitleToPathMap(item.getParentId());
        }

        Path pageMetsPath = pageTitleToPathMap.get(item.getTitle());
        notNull(pageMetsPath, () -> new NdkEnrichmentException("Path to METS file for page: " + item.getId() + " was not extracted from main METS file.",
                NDK_PAGE_DIV_NOT_FOUND));
        isTrue(Files.exists(pageMetsPath), () -> new NdkEnrichmentException("METS file for page: " + item.getId() + " in path: " + pageMetsPath +
                " does not exist.", NDK_PAGE_FILE_NOT_FOUND));

        return new NdkFilePathWrappedPage(item, pageMetsPath);
    }

    private void computePageTitleToPathMap(String publicationId) {
        Path mainMetsFilePath = metsFileLocatorService.locateMainMetsFile(publicationId);

        pageTitleToPathMap = metsExplorer.computeTitleToMetsMap(mainMetsFilePath);
    }

    @Autowired
    public void setMetsFileLocatorService(MetsFileLocatorService metsFileLocatorService) {
        this.metsFileLocatorService = metsFileLocatorService;
    }

    @Autowired
    public void setMetsExplorer(MetsExplorer metsExplorer) {
        this.metsExplorer = metsExplorer;
    }
}

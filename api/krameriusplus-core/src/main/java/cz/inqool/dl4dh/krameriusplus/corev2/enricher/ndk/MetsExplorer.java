package cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk;

import cz.inqool.dl4dh.krameriusplus.api.exception.NdkEnrichmentException;
import cz.inqool.dl4dh.mets.DivType;
import cz.inqool.dl4dh.mets.Mets;
import cz.inqool.dl4dh.mets.StructMapType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static cz.inqool.dl4dh.krameriusplus.api.exception.NdkEnrichmentException.ErrorCode.*;

@Component
public class MetsExplorer {

    private MetsUnmarshaller metsUnmarshaller;

    /**
     * Tries to extract a map from page title -> page mets file from the main METS file.
     * <p>
     * This is our best effort to map pages to their corresponding METS files with the information contained in
     * a mets:div element inside the main METS file, which look something like this:
     * <p>
     * <mets:div ID="DIV_P_PAGE_0000" ORDER="119" ORDERLABEL="[103]" TYPE="titlePage">
     * <mets:fptr FILEID="mc_0001"/>
     * <mets:fptr FILEID="uc_0001"/>
     * <mets:fptr FILEID="alto_0001"/>
     * <mets:fptr FILEID="txt_0001"/>
     * <mets:fptr FILEID="amd_0001"/>
     * </mets:div>
     * <p>
     * This is for the first page in a periodical volume (uuid:b23a8f60-d1a6-11ea-b7a2-005056827e51 in NDK). As we
     * can see, there is not much information to map divs to pages. Best we can do is to use ORDERLABEL and hope
     * that it matches Page::title, at least most of the time
     *
     * @param mainMetsFilePath path to the main METS file
     * @return map of page title -> PageMetsPath
     */
    public Map<String, Path> computeTitleToMetsMap(Path mainMetsFilePath) {
        Path pageMetsDirectory = findPageMetsDirectory(mainMetsFilePath);

        Mets mainMets = metsUnmarshaller.unmarshal(mainMetsFilePath);

        Optional<StructMapType> physicalStructMapElement = mainMets.getStructMap().stream()
                .filter(structMap -> structMap.getTYPE().equalsIgnoreCase("PHYSICAL"))
                .findFirst();
        if (physicalStructMapElement.isEmpty()) {
            throw new NdkEnrichmentException("Could not find <mets:structMap> element with TYPE='PHYSICAL' in main " +
                    "METS file: " + mainMetsFilePath, MAIN_METS_ERROR);
        }

        Map<String, Path> pageMetsFilenameToPathMap = getPageMetsMap(pageMetsDirectory);

        Map<String, Path> result = new HashMap<>();
        physicalStructMapElement.get().getDiv().getDiv().forEach(div -> {
            String pageTitle = div.getORDERLABEL();
            Path pageMetsPath = findPageMetsPath(pageMetsFilenameToPathMap, div);

            result.put(pageTitle, pageMetsPath);
        });

        return result;
    }

    private Map<String, Path> getPageMetsMap(Path pageMetsDirectory) {
        Map<String, Path> result = new HashMap<>();

        try (Stream<Path> pageMetsFiles = Files.list(pageMetsDirectory)) {
            pageMetsFiles.forEach(metsFile -> result.put(metsFile.getFileName().toString(), metsFile));
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }

        return result;
    }

    private Path findPageMetsDirectory(Path mainMetsFilePath) {
        Path pageMetsDirectory = mainMetsFilePath.resolveSibling("amdsec");
        if (!Files.exists(pageMetsDirectory)) {
            pageMetsDirectory = mainMetsFilePath.resolveSibling("amdSec");

            if (!Files.exists(pageMetsDirectory)) {
                throw new NdkEnrichmentException("Could not find folder with METS files for pages (tried '/amdsec' and '/amdSec').",
                        NDK_PAGE_DIRECTORY_NOT_FOUND);
            }
        }

        return pageMetsDirectory;
    }

    private Path findPageMetsPath(Map<String, Path> filenameToPathMap, DivType div) {
        DivType.Fptr amdFptrElement = div.getFptr().stream()
                .filter(fptr -> ((String) fptr.getFILEID()).startsWith("amd"))
                .findFirst()
                .orElseThrow(() -> new NdkEnrichmentException("Could not find <mets:fptr> element, where FILEID starts " +
                        "with 'amd' in <mets:div> with ORDERLABEL='" + div.getORDERLABEL() + "'.", NDK_PAGE_FILEID_NOT_FOUND));

        String fileIdValue = (String) amdFptrElement.getFILEID();
        String pageMetsFileSuffix = fileIdValue.substring(fileIdValue.length() - 4) + ".xml";

        return filenameToPathMap.entrySet().stream()
                .filter(entry -> entry.getKey().endsWith(pageMetsFileSuffix))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new NdkEnrichmentException("No METS file for page with suffix: " + pageMetsFileSuffix + " found.",
                        NDK_PAGE_FILE_NOT_FOUND));
    }

    @Autowired
    public void setMetsUnmarshaller(MetsUnmarshaller metsUnmarshaller) {
        this.metsUnmarshaller = metsUnmarshaller;
    }
}

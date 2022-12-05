package cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk;

import cz.inqool.dl4dh.krameriusplus.api.exception.NdkEnrichmentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.inqool.dl4dh.krameriusplus.api.exception.NdkEnrichmentException.ErrorCode.MAIN_METS_ERROR;
import static cz.inqool.dl4dh.krameriusplus.api.exception.NdkEnrichmentException.ErrorCode.NDK_PUBLICATION_DIRECTORY_NOT_FOUND;
import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.eq;
import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.isTrue;

@Component
@Slf4j
public class MetsFileLocatorService {

    private Path ndkDirectoryPath;

    /**
     * Searches for the METS file for given publicationId. First, it tries to find the folder in NDK Directory
     * (configured by ndkDirectoryPath), which name equals to publicationId without the 'uuid:' prefix. Then it tries
     * to find the METS file in this folder.
     *
     * @param publicationId ID of publication for which the folder should be searched for
     * @throws NdkEnrichmentException if NDK directory or main METS file was not found
     * @return Optional with Path if directory was found, otherwise empty optional
     */
    public Path locateMainMetsFile(String publicationId) {
        String publicationDirectoryName = publicationId.substring(5);
        Path publicationNdkDirectory = ndkDirectoryPath.resolve(publicationDirectoryName);
        isTrue(Files.exists(publicationNdkDirectory), () -> new NdkEnrichmentException("NDK directory '" + publicationNdkDirectory + "' not found.",
                NDK_PUBLICATION_DIRECTORY_NOT_FOUND));

        try (Stream<Path> files = Files.list(publicationNdkDirectory)) {
            List<Path> matchingFiles = files
                    .filter(this::matchesMetsPrefix)
                    .collect(Collectors.toList());

            eq(1, matchingFiles.size(), () -> new NdkEnrichmentException("Invalid number of main METS files matched for publication: " + publicationId +
                        ", expected: 1, actual: " + matchingFiles.size(), MAIN_METS_ERROR));

            return matchingFiles.get(0);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed listing dir content", e);
        }
    }

    private boolean matchesMetsPrefix(Path file) {
        return file.getFileName().startsWith("mets") || file.getFileName().startsWith("METS");
    }

    @Autowired
    public void setNdkDirectoryPath(@Value("${system.enrichment.ndk.path}") String ndkDirectoryPath) {
        this.ndkDirectoryPath = Path.of(ndkDirectoryPath);
        isTrue(Files.exists(this.ndkDirectoryPath), () ->
                new IllegalArgumentException("The configured NDK directory path '" + ndkDirectoryPath + "' does not exist."));
    }
}

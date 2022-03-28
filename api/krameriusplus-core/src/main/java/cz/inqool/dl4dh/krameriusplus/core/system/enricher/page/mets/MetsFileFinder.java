package cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.mets;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.publication.xml.XMLMetsUnmarshaller;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.publication.xml.dto.MainMetsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class MetsFileFinder {

    private final XMLMetsUnmarshaller metsUnmarshaller;

    private String ndkPath;

    @Autowired
    public MetsFileFinder(XMLMetsUnmarshaller metsUnmarshaller) {
        this.metsUnmarshaller = metsUnmarshaller;
    }

    public Optional<Path> findNdkPublicationDirectory(String publicationId) {
        if (ndkPath != null) {
            try (Stream<Path> ndkFiles = Files.list(Path.of(ndkPath))) {
                List<Path> matchingDirs = ndkFiles
                        .filter(Files::isDirectory)
                        .filter(dir -> dir.getFileName().toString().equals(publicationId.substring(5)))
                        .collect(Collectors.toList());

                if (matchingDirs.size() != 1) {
                    log.warn("NDK directory with id=\"" + publicationId + "\" not found");
                    return Optional.empty();
                }

                return Optional.of(matchingDirs.get(0));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to list files in folder '" + ndkPath + "'", e);
            }
        }

        log.warn("NDK directory not set");
        return Optional.empty();
    }

    public Path findMainMetsFile(Path ndkDir) {
        try (Stream<Path> files = Files.list(ndkDir)) {
            List<Path> matchingFiles = files
                    .filter(this::matchMainMetsFilename)
                    .collect(Collectors.toList());

            if (matchingFiles.size() != 1) {
                throw new IllegalStateException("Invalid number of METS files matched for publication, expected: 1, actual: " + matchingFiles.size());
            }

            return matchingFiles.get(0);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed listing dir content", e);
        }
    }

    /**
     * Tries to identify NDK files for pages by looking at the <mets:structMap TYPE="PHYSICAL"> elements in the
     * main mets file and matches {@link Page#getTitle()} with ORDERLABEL attribute in <mets:div> elements.
     * @param mainMetsPath
     */
    public List<MainMetsDto.Div> getMetsDivElements(Path mainMetsPath) {
        MainMetsDto mainMetsDto = metsUnmarshaller.unmarshal(mainMetsPath.toFile());

        Optional<MainMetsDto.StructMap> physicalStructureMap = getPhysicalStructureMap(mainMetsDto);

        if (physicalStructureMap.isPresent()) {
            return physicalStructureMap.get().getDiv().getDivs();
        }

        throw new IllegalStateException("MainMets file '" + mainMetsPath.getFileName().toString()
                + "' does not have mets:div elements under <mets:structMap TYPE=\"PHYSICAL\"> element");
    }

    private Optional<MainMetsDto.StructMap> getPhysicalStructureMap(MainMetsDto mainMetsDto) {
        return mainMetsDto.getStructMap()
                .stream()
                .filter(structMap -> structMap.getType().equalsIgnoreCase("PHYSICAL"))
                .findFirst();
    }

    private boolean matchMainMetsFilename(Path file) {
        return file.getFileName().toString().substring(0, 4).equalsIgnoreCase("mets")
                && file.getFileName().toString().endsWith(".xml");
    }

    @Autowired
    public void setNdkPath(@Value("${system.enrichment.ndk.path}") String path) {
        this.ndkPath = path;
    }
}

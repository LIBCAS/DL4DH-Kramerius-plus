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

    public Optional<Path> getMainMetsPath(String publicationId) {
        if (ndkPath != null) {
            try (Stream<Path> publicationNdkDirs = Files.list(Path.of(ndkPath))) {
                List<Path> matchingDirs = publicationNdkDirs
                        .filter(publicationNdkDir -> Files.isDirectory(publicationNdkDir)
                                && publicationNdkDir.getFileName().toString().equals(publicationId.substring(5)))
                        .collect(Collectors.toList());

                if (matchingDirs.size() != 1) {
                    log.warn("NDK directory with id=\"" + publicationId + "\" not found");
                    return Optional.empty();
                }

                return Optional.of(findMainMets(matchingDirs.get(0)));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        log.warn("NDK directory not set");
        return Optional.empty();
    }

    public void assignMetsPathForPages(List<Page> pages, Path mainMetsPath) {
        MainMetsDto mainMetsDto = metsUnmarshaller.unmarshal(mainMetsPath.toFile());

        List<MainMetsDto.StructMap> structMaps = mainMetsDto.getStructMap();

        for (MainMetsDto.StructMap structMap : structMaps) {
            if (structMap.getType().equals("PHYSICAL")) {
                for (MainMetsDto.Div pageInfo : structMap.getDiv().getDivs()) {
                    String pageMetsFilename = null;

                    for (var child : pageInfo.getChildren()) {
                        if (child.getFileId().startsWith("amd") || child.getFileId().startsWith("AMD")) {
                            pageMetsFilename = child.getFileId() + ".xml";
                        }
                    }

                    if (pageMetsFilename != null) {
                        Page correspondingPage = pages
                                .stream()
                                .filter(page -> page.getPageNumber() != null && page.getPageNumber().equals(pageInfo.getOrderLabel()))
                                .findFirst()
                                .orElse(null);

                        if (correspondingPage != null) {
                            Path metsPath = mainMetsPath.resolveSibling("amdSec").resolve(pageMetsFilename);

                            if (!Files.exists(metsPath)) {
                                metsPath = mainMetsPath.resolveSibling("amdsec").resolve(pageMetsFilename);
                            }

                            if (!Files.exists(metsPath)) {
                                log.warn("Mets path not found for page: {}", correspondingPage.getId());
                            } else {
                                correspondingPage.setMetsPath(metsPath.toString());
                            }
                        } else {
                            log.trace("METS file {} could not be associated with any page", pageMetsFilename);
                        }
                    }
                }
            }
        }
    }

    private Path findMainMets(Path ndkDir) {
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

    private boolean matchMainMetsFilename(Path file) {
        return (file.getFileName().toString().startsWith("mets")
                || file.getFileName().toString().startsWith("METS"))
                && file.toString().endsWith(".xml");
    }

    @Autowired
    public void setNdkPath(@Value("${system.enrichment.ndk.path}") String path) {
        this.ndkPath = path;
    }
}

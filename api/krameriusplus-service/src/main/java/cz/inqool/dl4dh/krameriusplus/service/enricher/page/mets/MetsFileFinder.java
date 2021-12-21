package cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.enricher.publication.xml.XMLMetsUnmarshaller;
import cz.inqool.dl4dh.krameriusplus.service.enricher.publication.xml.dto.MainMetsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MetsFileFinder {

    private final XMLMetsUnmarshaller metsUnmarshaller;

    @Autowired
    public MetsFileFinder(XMLMetsUnmarshaller metsUnmarshaller) {
        this.metsUnmarshaller = metsUnmarshaller;
    }

    public void setMetsPathForPages(List<Page> pages, Path ndkDir) {
        Path mainMets = findMainMets(ndkDir);

        MainMetsDto mainMetsDto = metsUnmarshaller.unmarshal(mainMets.toFile());

        List<MainMetsDto.StructMap> structMaps = mainMetsDto.getStructMap();

        for (MainMetsDto.StructMap structMap : structMaps) {
            if (structMap.getType().equals("PHYSICAL")) {
                for (MainMetsDto.Div pageInfo : structMap.getDiv().getDivs()) {
                    String pageMets = null;

                    for (var child : pageInfo.getChildren()) {
                        if (child.getFileId().startsWith("amd")) {
                            pageMets = child.getFileId() + ".xml";
                        }
                    }

                    if (pageMets == null) {
                        throw new IllegalStateException("Mets for page not found");
                    }

                    pages.get(pageInfo.getOrder() - 1).setMetsPath(ndkDir.resolve("amdsec").resolve(pageMets));
                }
            }
        }
    }

    private Path findMainMets(Path ndkDir) {
        try (Stream<Path> files = Files.list(ndkDir)) {
            List<Path> matchingFiles = files
                    .filter(file -> file.getFileName().toString().startsWith("mets") && file.toString().endsWith(".xml"))
                    .collect(Collectors.toList());

            if (matchingFiles.size() != 1) {
                throw new IllegalStateException("Invalid number of METS files matched for publication, expected: 1, actual: " + matchingFiles.size());
            }

            return matchingFiles.get(0);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed listing dir content", e);
        }
    }
}

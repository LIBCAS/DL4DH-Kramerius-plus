package cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.enricher.DomParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MetsFileFinder {

    private final DomParser domParser;

    @Autowired
    public MetsFileFinder(DomParser domParser) {
        this.domParser = domParser;
    }

    public Path findMetsForPage(Page page) {
        Path publicationNdkDir = page.getParent().getNdkDir().resolve("amdsec");

        return findMatchingMetsFile(publicationNdkDir, page);
    }

    private Path findMatchingMetsFile(Path dirPath, Page page) {
        try (Stream<Path> files = Files.list(dirPath)) {
            List<Path> matchingFiles = files
                    .filter(file -> containsUuid(file, page.getId()))
                    .collect(Collectors.toList());

            if (matchingFiles.size() != 1) {
                throw new IllegalStateException("Invalid number of METS files matched for given page, expected: 1, actual: " + matchingFiles.size());
            }

            return matchingFiles.get(0);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed listing dir content", e);
        }
    }

    private boolean containsUuid(Path file, String id) {
        Document document = domParser.parse(file.toFile());

        NodeList nodeList = document.getElementsByTagName("mix:objectIdentifierValue");

        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodeList.item(i);

                if (element.getTextContent().contains(id)) {
                    return true;
                }
            }
        }

        return false;
    }
}

package cz.inqool.dl4dh.krameriusplus.corev2.exporter;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.DigitalObjectExport;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.DigitalObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Exporter class for exporting DigitalObjects with additional metadata.csv file
 */
@Component
public class DigitalObjectExporter {

    public static final String[] METADATA_HEADERS = new String[]{"UUID", "FILENAME", "MODEL", "INDEX", "TITLE"};

    public void export(List<? extends DigitalObjectExport> objectsToExport, Path destination) throws IOException {
        try (CSVPrinter printer = getPrinter(destination)) {
            for (DigitalObjectExport item : objectsToExport) {
                String itemFilename = item.getFilename();
                Path file = destination.resolve(itemFilename);
                Files.createFile(file);

                Files.write(file, item.getContent().getBytes(StandardCharsets.UTF_8));

                printer.printRecord(toRecord(item, itemFilename));
            }
        }
    }

    private Iterable<?> toRecord(DigitalObjectExport item, String itemFilename) {
        DigitalObject digitalObject = item.getDigitalObject();
        return List.of(
                digitalObject.getId(),
                itemFilename,
                item.getDigitalObject().getModel(),
                digitalObject.getIndex(),
                digitalObject.getTitle());
    }

    private CSVPrinter getPrinter(Path destination) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(destination.resolve("metadata.csv"));
        return new CSVPrinter(writer, CSVFormat.Builder.create().setHeader(METADATA_HEADERS).build());
    }
}

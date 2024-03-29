package cz.inqool.dl4dh.krameriusplus.core.exporter;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.wrapper.DigitalObjectExport;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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

                if (item.getDigitalObject() != null) {
                    printer.printRecord(toRecord(item.getDigitalObject(), itemFilename));
                }
            }
        }
    }

    private Iterable<?> toRecord(DigitalObject digitalObject, String itemFilename) {
        return digitalObject.getIndex() == null ?
                objectRecord(digitalObject, itemFilename) : indexedObjectRecord(digitalObject, itemFilename);

    }

    private Iterable<?> indexedObjectRecord(DigitalObject indexed, String itemFilename) {
        return List.of(
                indexed.getId(),
                itemFilename,
                indexed.getModel(),
                indexed.getIndex(),
                indexed.getTitle());
    }

    private Iterable<?> objectRecord(DigitalObject digitalObject, String itemFilename) {
        return List.of(
                digitalObject.getId(),
                itemFilename,
                "",
                digitalObject.getModel(),
                digitalObject.getTitle());
    }

    private CSVPrinter getPrinter(Path destination) throws IOException {
        CSVFormat csvFormat = Files.exists(destination.resolve("metadata.csv")) ?
                CSVFormat.Builder.create().setHeader(METADATA_HEADERS).setSkipHeaderRecord(true).build() :
                CSVFormat.Builder.create().setHeader(METADATA_HEADERS).build();


        BufferedWriter writer = Files.newBufferedWriter(destination.resolve("metadata.csv"), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        return new CSVPrinter(writer, csvFormat);
    }
}

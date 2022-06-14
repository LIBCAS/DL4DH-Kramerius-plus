package cz.inqool.dl4dh.krameriusplus.service.system.exporter;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.lindat.udpipe.Token;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

public class CsvExporter {

    private final String DELIMITER;

    public CsvExporter(String delimiter) {
        DELIMITER = delimiter;
    }

    private final List<String> PAGES_HEADER = List.of(
            "tokenIndex", "token", "startOffset", "endOffset",
            "position", "lemma", "uPosTag", "xPosTag", "feats", "misc",
            "nameTag");

//    private final List<String> PUBLICATION_HEADER = List.of(
//            "id", "title",
//            "mods.name.type", "mods.name.identifier", "mods.name.part",
//            "mods.genre", ""
//    )

    public void export(Page page, OutputStream outputStream) {
        CSVFormat format = CSVFormat.Builder.create().setDelimiter(DELIMITER).build();

        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream);
             CSVPrinter printer = new CSVPrinter(writer, format)) {
            printer.printRecord(PAGES_HEADER);

            for (Token token : page.getTokens()) {
                printer.printRecord(prepareLine(token));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error writing page", e);
        }
    }

    public void export(Publication publication, OutputStream outputStream) {
        throw new UnsupportedOperationException("Not sure if object of type 'Publication' should be part of the CSV export.");
    }

    private List<String> prepareLine(Token token) {
        List<String> line = new ArrayList<>();
        line.add(token.getTokenIndex().toString());
        line.add(token.getContent());
        line.add(token.getStartOffset().toString());
        line.add(token.getEndOffset().toString());

        line.add(token.getLinguisticMetadata().getPosition().toString());
        line.add(token.getLinguisticMetadata().getLemma());
        line.add(token.getLinguisticMetadata().getUPosTag());
        line.add(token.getLinguisticMetadata().getXPosTag());
        line.add(token.getLinguisticMetadata().getFeats());
        line.add(token.getLinguisticMetadata().getMisc());

        line.add(token.getNameTagMetadata());

        return line;
    }
}

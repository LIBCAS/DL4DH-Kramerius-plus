package cz.inqool.dl4dh.krameriusplus.service.system.exporter;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.lindat.udpipe.Token;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

public class CsvExporter {

    private final char DELIMITER;

    public CsvExporter(char delimiter) {
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
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT).setDelimiter(DELIMITER).setQuoteMode(QuoteMode.NONE).build();

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
        line.add(toStringValue(token.getTokenIndex()));
        line.add(toStringValue(token.getContent()));
        line.add(toStringValue(token.getStartOffset()));
        line.add(toStringValue(token.getEndOffset()));

        if (token.getLinguisticMetadata() != null) {
            line.add(toStringValue(token.getLinguisticMetadata().getPosition()));
            line.add(toStringValue(token.getLinguisticMetadata().getLemma()));
            line.add(toStringValue(token.getLinguisticMetadata().getUPosTag()));
            line.add(toStringValue(token.getLinguisticMetadata().getXPosTag()));
            line.add(toStringValue(token.getLinguisticMetadata().getFeats()));
            line.add(toStringValue(token.getLinguisticMetadata().getMisc()));
        } else {
            for (int i = 0; i < 6; i++) {
                line.add("");
            }
        }

        line.add(toStringValue(token.getNameTagMetadata()));

        return line;
    }

    private String toStringValue(Integer fieldValue) {
        if (fieldValue == null) {
            return "";
        }

        return String.valueOf(fieldValue);
    }

    private String toStringValue(String fieldValue) {
        if (fieldValue == null) {
            return "";
        }

        return fieldValue;
    }
}

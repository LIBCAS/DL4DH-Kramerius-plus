package cz.inqool.dl4dh.krameriusplus.core.batch.step;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsGenre;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsMetadata;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsNamePart;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Token;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CsvExporter {

    private final CSVFormat format;

    public CsvExporter(char delimiter) {
        format = CSVFormat.Builder
                .create(CSVFormat.DEFAULT)
                .setDelimiter(delimiter)
                .setEscape('"')
                .setQuoteMode(QuoteMode.NONE)
                .build();
    }

    private final List<String> PAGES_HEADER = List.of(
            "tokenIndex", "token", "startOffset", "endOffset",
            "position", "lemma", "uPosTag", "xPosTag", "feats", "misc",
            "nameTag");

    private final List<String> PUBLICATION_HEADER = List.of(
            "id", "title", "policy",
            "mods.name.type", "mods.name.identifier", "mods.name.parts",
            "mods.genre", "mods.originInfo.publishers", "mods.physicalDescription.extent");

    public void export(Page page, OutputStream outputStream) {
        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream);
             CSVPrinter printer = new CSVPrinter(writer, format)) {
            printer.printRecord(PAGES_HEADER);

            for (Token token : page.getTokens()) {
                printer.printRecord(prepareLine(token));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error writing page to CSV file", e);
        }
    }

    public void export(Publication publication, OutputStream outputStream) {
        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream);
             CSVPrinter printer = new CSVPrinter(writer, format)) {
            printer.printRecord(PUBLICATION_HEADER);

            printer.printRecord(preparePublicationLine(publication));
        } catch (IOException e) {
            throw new UncheckedIOException("Error writing publication to CSV file", e);
        }
    }

    private List<String> preparePublicationLine(Publication publication) {
        List<String> line = new ArrayList<>();
        line.add(publication.getId());
        line.add(publication.getTitle());
        line.add(publication.getPolicy());

        if (publication.getModsMetadata() != null) {
            ModsMetadata mods = publication.getModsMetadata();
            if (mods.getName() != null) {
                line.add(mods.getName().getType());
                line.add(mods.getName().getNameIdentifier());
                line.add(listToCsvString(mods.getName().getNameParts()
                        .stream().map(ModsNamePart::getValue)
                        .collect(Collectors.toList())));
            } else {
                addEmptyValues(line, 3);
            }
            line.add(listToCsvString(mods.getGenres().stream()
                    .map(ModsGenre::getValue)
                    .collect(Collectors.toList())));

            if (mods.getOriginInfo() != null) {
                line.add(listToCsvString(mods.getOriginInfo().stream()
                        .map(modsOriginInfo -> listToCsvString(modsOriginInfo.getPublishers()))
                        .collect(Collectors.toList())));
            } else {
                addEmptyValues(line, 1);
            }

            if (mods.getPhysicalDescription() != null) {
                line.add(mods.getPhysicalDescription().getExtent());
            } else {
                addEmptyValues(line, 1);
            }
        } else {
            addEmptyValues(line, 6);
        }

        return line;
    }

    private String listToCsvString(List<String> strings) {
        return "[" + String.join(",", strings) + "]";
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
            addEmptyValues(line, 6);
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

    private void addEmptyValues(List<String> line, int amount) {
        for (int i = 0; i < amount; i++) {
            line.add(null);
        }
    }
}


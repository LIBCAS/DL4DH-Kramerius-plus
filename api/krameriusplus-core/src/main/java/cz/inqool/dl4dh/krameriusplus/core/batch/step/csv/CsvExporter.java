package cz.inqool.dl4dh.krameriusplus.core.batch.step.csv;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsMetadata;
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

public class CsvExporter {

    private final CSVFormat format;

    private final ObjectMapper objectMapper = initObjectMapper();

    public CsvExporter(char delimiter) {
        format = CSVFormat.Builder
                .create(CSVFormat.DEFAULT)
                .setDelimiter(delimiter)
                .setEscape('\\')
                .setQuoteMode(QuoteMode.MINIMAL)
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

    private List<String> preparePublicationLine(Publication publication) throws JsonProcessingException {
        List<String> line = new ArrayList<>();
        line.add(publication.getId());
        line.add(publication.getTitle());
        line.add(publication.getPolicy());

        if (publication.getModsMetadata() != null) {
            ModsMetadata mods = publication.getModsMetadata();
            if (mods.getNames() != null) {
                line.add(objectMapper.writeValueAsString(mods.getNames()));
                line.add(objectMapper.writeValueAsString(mods.getNames()));
                line.add(objectMapper.writeValueAsString(mods.getNames()));
            } else {
                addEmptyValues(line, 3);
            }
            line.add(objectMapper.writeValueAsString(mods.getGenres()));

            if (mods.getOriginInfos() != null) {
                line.add(objectMapper.writeValueAsString(mods.getOriginInfos()));
            } else {
                addEmptyValues(line, 1);
            }

            if (mods.getPhysicalDescriptions() != null) {
                line.add(objectMapper.writeValueAsString(mods.getPhysicalDescriptions()));
            } else {
                addEmptyValues(line, 1);
            }
        } else {
            addEmptyValues(line, 6);
        }

        return line;
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

    private ObjectMapper initObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("UnQuote");
        module.addSerializer(new NoQuotesSerializer());

        objectMapper.disable(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature());
        objectMapper.disable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.registerModule(module);

        return objectMapper;
    }
}


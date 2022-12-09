package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.AltoStringWrappedPage;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.ExecutionContextKey.DIRECTORY;

@Component
@StepScope
public class AltoFileItemWriter implements ItemWriter<AltoStringWrappedPage> {

    public static final String[] HEADERS = new String[]{"UUID", "FILENAME", "INDEX", "TITLE"};

    private Path directory;

    @Override
    public void write(List<? extends AltoStringWrappedPage> items) throws Exception {
        try (CSVPrinter printer = getPrinter()) {
            for (AltoStringWrappedPage item : items) {
                String itemFilename = item.getPage().getId().substring(5) + ".xml";
                Path file = directory.resolve(itemFilename);
                Files.createFile(file);

                Files.write(file, item.getAlto().getBytes(StandardCharsets.UTF_8));

                printer.printRecord(toRecord(item, itemFilename));
            }
        }
    }

    private Iterable<?> toRecord(AltoStringWrappedPage item, String itemFilename) {
        Page page = item.getPage();
        return List.of(page.getId(), itemFilename, page.getIndex(), page.getTitle());
    }

    private CSVPrinter getPrinter() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(directory.resolve("metadata.csv"));
        return new CSVPrinter(writer, CSVFormat.Builder.create().setHeader(HEADERS).build());
    }

    @Autowired
    public void setDirectory(@Value("#{jobContext['" + DIRECTORY + "']}") String directory) {
        this.directory = Path.of(directory);
    }
}

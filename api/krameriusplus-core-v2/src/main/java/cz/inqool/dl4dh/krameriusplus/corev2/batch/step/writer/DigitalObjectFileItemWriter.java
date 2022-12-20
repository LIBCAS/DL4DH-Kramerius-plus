package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.DigitalObjectExport;
import cz.inqool.dl4dh.krameriusplus.corev2.exporter.DigitalObjectExporter;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.ExecutionContextKey.DIRECTORY;

@Component
@StepScope
public class DigitalObjectFileItemWriter implements ItemWriter<DigitalObjectExport> {

    private Path directory;

    private DigitalObjectExporter exporter;

    @Override
    public void write(List<? extends DigitalObjectExport> items) throws Exception {
        exporter.export(items, directory);
    }

    @Autowired
    public void setDirectory(@Value("#{stepContext['" + DIRECTORY + "']}") String directory) {
        this.directory = Path.of(directory);
    }

    @Autowired
    public void setExporter(DigitalObjectExporter exporter) {
        this.exporter = exporter;
    }
}

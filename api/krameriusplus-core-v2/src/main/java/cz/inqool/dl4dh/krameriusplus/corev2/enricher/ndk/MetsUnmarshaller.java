package cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk;

import cz.inqool.dl4dh.mets.Mets;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.nio.file.Path;

@Component
public class MetsUnmarshaller {

    private final Unmarshaller unmarshaller;

    public MetsUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Mets.class);
        this.unmarshaller = context.createUnmarshaller();
    }

    public Mets unmarshal(Path path) {
        try {
            return (Mets) unmarshaller.unmarshal(path.toFile());
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Could not unmarshall given file", e);
        }
    }
}

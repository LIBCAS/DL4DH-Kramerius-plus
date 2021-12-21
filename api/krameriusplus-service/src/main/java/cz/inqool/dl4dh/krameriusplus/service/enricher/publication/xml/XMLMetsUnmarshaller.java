package cz.inqool.dl4dh.krameriusplus.service.enricher.publication.xml;

import cz.inqool.dl4dh.krameriusplus.service.enricher.publication.xml.dto.MainMetsDto;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Component
public class XMLMetsUnmarshaller {

    private final Unmarshaller unmarshaller;

    public XMLMetsUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(MainMetsDto.class);
        unmarshaller = context.createUnmarshaller();
    }

    public MainMetsDto unmarshal(File file) {
        try {
            return (MainMetsDto) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Could not unmarshall given file", e);
        }
    }
}

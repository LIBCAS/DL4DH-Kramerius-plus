package cz.inqool.dl4dh.krameriusplus.core.enricher.ndk.converter;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisEventElement;
import cz.inqool.dl4dh.krameriusplus.core.enricher.ndk.mapper.PremisEventMapper;
import cz.inqool.dl4dh.ndk.premis.EventComplexType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXB;
import java.io.StringReader;

@Component
public class PremisEventConverter implements MetsConverter<MetsPremisEventElement> {

    private PremisEventMapper mapper;

    @Override
    public MetsPremisEventElement convert(String source, String elementId) {
        EventComplexType extracted = JAXB.unmarshal(new StringReader(source), EventComplexType.class);

        MetsPremisEventElement metsPremisEventElement = mapper.convert(extracted);
        metsPremisEventElement.setId(elementId);

        return metsPremisEventElement;
    }

    @Override
    public Class<MetsPremisEventElement> supports() {
        return MetsPremisEventElement.class;
    }

    @Autowired
    public void setMapper(PremisEventMapper mapper) {
        this.mapper = mapper;
    }
}

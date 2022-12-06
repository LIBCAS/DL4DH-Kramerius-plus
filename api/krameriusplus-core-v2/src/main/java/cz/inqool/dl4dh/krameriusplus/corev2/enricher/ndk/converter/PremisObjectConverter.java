package cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk.converter;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsPremisObjectElement;
import cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk.mapper.PremisObjectMapper;
import cz.inqool.dl4dh.ndk.premis.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXB;
import java.io.StringReader;

@Component
public class PremisObjectConverter implements MetsConverter<MetsPremisObjectElement> {

    private PremisObjectMapper mapper;

    @Override
    public MetsPremisObjectElement convert(String source, String elementId) {
        File extracted = JAXB.unmarshal(new StringReader(source), File.class);

        MetsPremisObjectElement result = mapper.convert(extracted);
        result.setId(elementId);

        return result;
    }

    @Override
    public Class<MetsPremisObjectElement> supports() {
        return MetsPremisObjectElement.class;
    }

    @Autowired
    public void setMapper(PremisObjectMapper mapper) {
        this.mapper = mapper;
    }
}

package cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk.converter;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement;
import cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk.mapper.MixMapper;
import cz.inqool.dl4dh.ndk.mix.MixType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXB;
import java.io.StringReader;

@Component
public class MixConverter implements MetsConverter<MetsMixElement> {

    private MixMapper mapper;

    @Override
    public MetsMixElement convert(String source, String elementId) {
        MixType extracted = JAXB.unmarshal(new StringReader(source), MixType.class);

        MetsMixElement converted = mapper.convert(extracted);
        converted.setId(elementId);

        return converted;
    }

    @Override
    public Class<MetsMixElement> supports() {
        return MetsMixElement.class;
    }

    @Autowired
    public void setMapper(MixMapper mapper) {
        this.mapper = mapper;
    }
}

package cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.api.publication.mods.ModsMetadata;
import cz.inqool.dl4dh.krameriusplus.core.enricher.mods.ModsMapper;
import cz.inqool.dl4dh.krameriusplus.core.kramerius.KrameriusMessenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModsProvider {

    private KrameriusMessenger krameriusMessenger;

    private ModsMapper modsMapper;

    public ModsMetadata getMods(String publicationId) {
        return modsMapper.map(krameriusMessenger.getMods(publicationId));
    }

    @Autowired
    public void setKrameriusMessenger(KrameriusMessenger krameriusMessenger) {
        this.krameriusMessenger = krameriusMessenger;
    }

    @Autowired
    public void setModsMapper(ModsMapper modsMapper) {
        this.modsMapper = modsMapper;
    }
}

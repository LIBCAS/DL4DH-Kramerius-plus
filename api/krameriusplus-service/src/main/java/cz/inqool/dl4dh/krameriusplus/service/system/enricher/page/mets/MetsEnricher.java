package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.MetsMetadata;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.agent.MetsPremisAgentElement;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.event.MetsPremisEventElement;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.mix.MetsMixElement;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.object.MetsPremisObjectElement;
import cz.inqool.dl4dh.mets.AmdSecType;
import cz.inqool.dl4dh.mets.MdSecType;
import cz.inqool.dl4dh.mets.Mets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXB;
import java.nio.file.Path;
import java.util.List;

@Service
public class MetsEnricher {

    private final MetsExtractor metsExtractor;

    @Autowired
    public MetsEnricher(MetsExtractor metsExtractor) {
        this.metsExtractor = metsExtractor;
    }

    public void enrich(Page page) {
        Path metsPath = Path.of(page.getNdkFilePath());

        if (metsPath == null) {
            return;
        }

        Mets document = JAXB.unmarshal(metsPath.toFile(), Mets.class);

        MetsMetadata metsMetadata = new MetsMetadata();

        AmdSecType amdSec = document.getAmdSec().get(0);
        List<MdSecType> techMDNodes = amdSec.getTechMD();
        List<MdSecType> digiprovMDNodes = amdSec.getDigiprovMD();

        metsMetadata.setPremisObjects(metsExtractor.extract(techMDNodes, "OBJ", MetsPremisObjectElement.class));
        metsMetadata.setMix(metsExtractor.extract(techMDNodes, "MIX", MetsMixElement.class));
        metsMetadata.setPremisEvents(metsExtractor.extract(digiprovMDNodes, "EVT", MetsPremisEventElement.class));
        metsMetadata.setPremisAgents(metsExtractor.extract(digiprovMDNodes, "AGENT", MetsPremisAgentElement.class));

        page.setMetsMetadata(metsMetadata);
    }
}

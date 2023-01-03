package cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.*;
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

    public MetsMetadata extractMetadata(Path metsPath) {
        Mets document = JAXB.unmarshal(metsPath.toFile(), Mets.class);

        MetsMetadata metsMetadata = new MetsMetadata();

        AmdSecType amdSec = document.getAmdSec().get(0);
        List<MdSecType> techMDNodes = amdSec.getTechMD();
        List<MdSecType> digiprovMDNodes = amdSec.getDigiprovMD();

        metsMetadata.setPremisObjects(metsExtractor.extract(techMDNodes, "OBJ", MetsPremisObjectElement.class));
        metsMetadata.setMix(metsExtractor.extract(techMDNodes, "MIX", MetsMixElement.class));
        metsMetadata.setPremisEvents(metsExtractor.extract(digiprovMDNodes, "EVT", MetsPremisEventElement.class));
        metsMetadata.setPremisAgents(metsExtractor.extract(digiprovMDNodes, "AGENT", MetsPremisAgentElement.class));

        return metsMetadata;
    }
}

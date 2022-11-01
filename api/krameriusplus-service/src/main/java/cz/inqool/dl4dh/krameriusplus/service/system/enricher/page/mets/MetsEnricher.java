package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.MetsMetadata;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.DomParser;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.valueextractors.MixExtractor;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.valueextractors.PremisAgentExtractor;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.valueextractors.PremisEventExtractor;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.valueextractors.PremisObjectExtractor;
import cz.inqool.dl4dh.mets.MdSecType;
import cz.inqool.dl4dh.mets.Mets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXB;
import java.nio.file.Path;
import java.util.List;

@Service
public class MetsEnricher {

    private final DomParser domParser;

    @Autowired
    public MetsEnricher(DomParser domParser) {
        this.domParser = domParser;
    }

    public void enrich(Page page) {
        Path metsPath = Path.of(page.getNdkFilePath());

        if (metsPath == null) {
            return;
        }

        Mets document = JAXB.unmarshal(page.getNdkFilePath(), Mets.class);

        MetsMetadata metsMetadata = new MetsMetadata();

        List<MdSecType> techMDNodes = document.getAmdSec().get(0).getTechMD();
        List<MdSecType> digiprovMDNodes = document.getAmdSec().get(0).getDigiprovMD();

        metsMetadata.setPremisObjects(new PremisObjectExtractor(domParser).extract(techMDNodes));
        metsMetadata.setPremisEvents(new PremisEventExtractor(domParser).extract(digiprovMDNodes));
        metsMetadata.setPremisAgents(new PremisAgentExtractor(domParser).extract(digiprovMDNodes));
        metsMetadata.setMix(new MixExtractor(domParser).extract(techMDNodes));

        page.setMetsMetadata(metsMetadata);
    }
}

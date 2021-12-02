package cz.inqool.dl4dh.krameriusplus.service;

import cz.inqool.dl4dh.krameriusplus.service.enricher.DomParser;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets.MetsExtractor;

import java.nio.file.Path;

public abstract class MetsMetadataExtractorTest {

    protected final Path metsFile = Path.of("C:\\inqool\\dl4dh\\ndk_balicky\\kersch_ndk_balicky_v3\\monografie\\aba007-0004l6\\amdsec\\amd_mets_aba007-0004l6_0007.xml");

    protected final MetsExtractor metsExtractor = new MetsExtractor(new DomParser());
}

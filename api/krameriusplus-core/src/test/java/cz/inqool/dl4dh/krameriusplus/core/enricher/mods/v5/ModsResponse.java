package cz.inqool.dl4dh.krameriusplus.core.enricher.mods.v5;

class ModsResponse {

    // obtained from http://kramerius.mzk.cz/search/api/v5.0/item/uuid:6a7a9842-faff-4af7-a502-a1229611e67f/streams/BIBLIO_MODS
     static final String MONOGRAPH_MODS = "\n" +
            "<mods:modsCollection xmlns=\"http://www.w3.org/1999/xlink\" xmlns:mods=\"http://www.loc.gov/mods/v3\">\n" +
            "    <mods:mods ID=\"MODS_VOLUME_0001\" version=\"3.6\">\n" +
            "        <mods:titleInfo>\n" +
            "            <mods:title>Výkonnost open source aplikací</mods:title>\n" +
            "            <mods:subTitle>rychlost, přesnost a trocha štěstí</mods:subTitle>\n" +
            "        </mods:titleInfo>\n" +
            "        <mods:titleInfo type=\"uniform\">\n" +
            "            <mods:title>Performance of open source applications. Česky</mods:title>\n" +
            "        </mods:titleInfo>\n" +
            "        <mods:name type=\"personal\">\n" +
            "            <mods:namePart>Armstrong, Tavish</mods:namePart>\n" +
            "            <mods:role>\n" +
            "                <mods:roleTerm authority=\"marcrelator\" type=\"code\">edt</mods:roleTerm>\n" +
            "            </mods:role>\n" +
            "        </mods:name>\n" +
            "        <mods:typeOfResource>text</mods:typeOfResource>\n" +
            "        <mods:genre authority=\"rdacontent\">text</mods:genre>\n" +
            "        <mods:genre authority=\"czenas\">případové studie</mods:genre>\n" +
            "        <mods:genre authority=\"eczenas\">case studies</mods:genre>\n" +
            "        <mods:originInfo>\n" +
            "            <mods:place>\n" +
            "                <mods:placeTerm authority=\"marccountry\" type=\"code\">xr</mods:placeTerm>\n" +
            "            </mods:place>\n" +
            "            <mods:dateIssued encoding=\"marc\">2016</mods:dateIssued>\n" +
            "            <mods:edition>1. vydání</mods:edition>\n" +
            "            <mods:issuance>monographic</mods:issuance>\n" +
            "        </mods:originInfo>\n" +
            "        <mods:originInfo eventType=\"publication\">\n" +
            "            <mods:place>\n" +
            "                <mods:placeTerm type=\"text\">Praha :</mods:placeTerm>\n" +
            "            </mods:place>\n" +
            "            <mods:publisher>CZ.NIC, z.s.p.o.,</mods:publisher>\n" +
            "            <mods:dateIssued>2016</mods:dateIssued>\n" +
            "        </mods:originInfo>\n" +
            "        <mods:language>\n" +
            "            <mods:languageTerm authority=\"iso639-2b\" type=\"code\">cze</mods:languageTerm>\n" +
            "        </mods:language>\n" +
            "        <mods:language objectPart=\"translation\">\n" +
            "            <mods:languageTerm authority=\"iso639-2b\" type=\"code\">eng</mods:languageTerm>\n" +
            "        </mods:language>\n" +
            "        <mods:physicalDescription>\n" +
            "            <mods:form authority=\"marcform\">print</mods:form>\n" +
            "            <mods:form authority=\"marccategory\">text</mods:form>\n" +
            "            <mods:form authority=\"marcsmd\">regular print</mods:form>\n" +
            "            <mods:extent>264 stran : ilustrace ; 25 cm</mods:extent>\n" +
            "            <mods:form authority=\"rdamedia\" type=\"media\">bez média</mods:form>\n" +
            "            <mods:form authority=\"rdacarrier\" type=\"carrier\">svazek</mods:form>\n" +
            "        </mods:physicalDescription>\n" +
            "        <mods:targetAudience authority=\"marctarget\">specialized</mods:targetAudience>\n" +
            "        <mods:note altRepGroup=\"00\" type=\"statement of responsibility\">editoval Tavish Armstrong</mods:note>\n" +
            "        <mods:note>Přeloženo z angličtiny</mods:note>\n" +
            "        <mods:note type=\"bibliography\">Obsahuje bibliografii a bibliografické odkazy</mods:note>\n" +
            "        <mods:subject authority=\"czenas\">\n" +
            "            <mods:topic>otevřený software</mods:topic>\n" +
            "        </mods:subject>\n" +
            "        <mods:subject authority=\"czenas\">\n" +
            "            <mods:topic>vývoj softwaru</mods:topic>\n" +
            "        </mods:subject>\n" +
            "        <mods:subject authority=\"czenas\">\n" +
            "            <mods:topic>ladění softwaru</mods:topic>\n" +
            "        </mods:subject>\n" +
            "        <mods:subject authority=\"czenas\">\n" +
            "            <mods:topic>optimalizační metody</mods:topic>\n" +
            "        </mods:subject>\n" +
            "        <mods:subject>\n" +
            "            <mods:topic>open source software</mods:topic>\n" +
            "        </mods:subject>\n" +
            "        <mods:subject>\n" +
            "            <mods:topic>software development</mods:topic>\n" +
            "        </mods:subject>\n" +
            "        <mods:subject>\n" +
            "            <mods:topic>software debugging</mods:topic>\n" +
            "        </mods:subject>\n" +
            "        <mods:subject>\n" +
            "            <mods:topic>optimization methods</mods:topic>\n" +
            "        </mods:subject>\n" +
            "        <mods:classification authority=\"udc\">004.4.057.8</mods:classification>\n" +
            "        <mods:classification authority=\"udc\">004.415</mods:classification>\n" +
            "        <mods:classification authority=\"udc\">004.416.2</mods:classification>\n" +
            "        <mods:classification authority=\"udc\">004.055</mods:classification>\n" +
            "        <mods:classification authority=\"udc\">(078.7)</mods:classification>\n" +
            "        <mods:location>\n" +
            "            <mods:url displayLabel=\"Digitalizovaný dokument\" note=\"pdf\" usage=\"primary display\">http://www.digitalniknihovna.cz/mzk/view/uuid:6a7a9842-faff-4af7-a502-a1229611e67f</mods:url>\n" +
            "            <mods:physicalLocation authority=\"siglaADR\">BOA001</mods:physicalLocation>\n" +
            "            <mods:shelfLocator>2-1373.309</mods:shelfLocator>\n" +
            "            <mods:physicalLocation>BOA001</mods:physicalLocation>\n" +
            "        </mods:location>\n" +
            "        <mods:relatedItem type=\"series\">\n" +
            "            <mods:titleInfo>\n" +
            "                <mods:title>CZ.NIC</mods:title>\n" +
            "            </mods:titleInfo>\n" +
            "        </mods:relatedItem>\n" +
            "        <mods:identifier type=\"isbn\">978-80-88168-11-9</mods:identifier>\n" +
            "        <mods:identifier type=\"oclc\">958229742</mods:identifier>\n" +
            "        <mods:recordInfo>\n" +
            "            <mods:descriptionStandard>rda</mods:descriptionStandard>\n" +
            "            <mods:recordContentSource authority=\"marcorg\">OLA001</mods:recordContentSource>\n" +
            "            <mods:recordCreationDate encoding=\"marc\">160819</mods:recordCreationDate>\n" +
            "            <mods:recordChangeDate encoding=\"iso8601\">20180815163600.0</mods:recordChangeDate>\n" +
            "            <mods:recordIdentifier source=\"CZ PrNK\">nkc20162827815</mods:recordIdentifier>\n" +
            "            <mods:recordOrigin>Converted from MARCXML to MODS version 3.6 using MARC21slim2MODS3-6.xsl\n" +
            "\t\t\t\t(Revision 1.119 2018/06/21)</mods:recordOrigin>\n" +
            "            <mods:languageOfCataloging>\n" +
            "                <mods:languageTerm authority=\"iso639-2b\" type=\"code\">cze</mods:languageTerm>\n" +
            "            </mods:languageOfCataloging>\n" +
            "        </mods:recordInfo>\n" +
            "        <mods:identifier type=\"uuid\">uuid:6a7a9842-faff-4af7-a502-a1229611e67f</mods:identifier>\n" +
            "        <mods:identifier type=\"sysno\">MZK01-001539808</mods:identifier>\n" +
            "        <mods:identifier type=\"ccnb\">cnb002827815</mods:identifier>\n" +
            "        <mods:identifier type=\"oclc\">958229742</mods:identifier>\n" +
            "        <mods:identifier type=\"barCode\">2610679857</mods:identifier>\n" +
            "    </mods:mods>\n" +
            "</mods:modsCollection>";
}

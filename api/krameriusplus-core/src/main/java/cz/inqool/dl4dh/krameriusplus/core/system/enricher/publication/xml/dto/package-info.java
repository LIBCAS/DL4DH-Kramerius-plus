@XmlSchema(
        namespace = "http://www.loc.gov/METS/",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
                @XmlNs(prefix = "mets", namespaceURI = "http://www.loc.gov/METS/"),
                @XmlNs(prefix = "dc", namespaceURI = "http://purl.org/dc/elements/1.1/"),
                @XmlNs(prefix = "mods", namespaceURI = "http://www.loc.gov/mods/v3"),
                @XmlNs(prefix = "oai_dc", namespaceURI = "http://www.openarchives.org/OAI/2.0/oai_dc/"),
                @XmlNs(prefix = "xlink", namespaceURI = "http://www.w3.org/1999/xlink")
        }
)
package cz.inqool.dl4dh.krameriusplus.core.system.enricher.publication.xml.dto;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
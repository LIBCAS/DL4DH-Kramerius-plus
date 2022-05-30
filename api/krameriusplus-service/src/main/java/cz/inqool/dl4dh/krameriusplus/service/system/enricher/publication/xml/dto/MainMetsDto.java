package cz.inqool.dl4dh.krameriusplus.service.system.enricher.publication.xml.dto;

import lombok.Getter;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlType
@XmlRootElement(name = "mets", namespace = "http://www.loc.gov/METS/")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class MainMetsDto {

    @XmlElement(name = "structMap")
    private List<StructMap> structMap;

    @Getter
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class StructMap {

        @XmlAttribute(name = "TYPE")
        private String type;

        @XmlElement(name = "div")
        private DivContainer div;
    }

    @Getter
    public static class DivContainer {

        @XmlAttribute(name = "ID")
        private String id;

        @XmlAttribute(name = "LABEL")
        private String label;

        @XmlAttribute(name = "TYPE")
        private String type;

        @XmlElement(name = "div")
        private List<Div> divs;
    }

    @Getter
    public static class Div {

        @XmlAttribute(name = "ID")
        private String id;

        @XmlAttribute(name = "ORDER")
        private int order;

        @XmlAttribute(name = "ORDERLABEL")
        private String orderLabel;

        @XmlAttribute(name = "TYPE")
        private String type;

        @XmlElement(name = "fptr")
        private List<DivChild> children;
    }

    @Getter
    public static class DivChild {

        @XmlAttribute(name = "FILEID")
        private String fileId;
    }


}

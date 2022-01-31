package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.object;

import cz.inqool.dl4dh.krameriusplus.domain.XMLUtils;
import info.lc.xmlns.premis_v2.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.JAXBElement;
import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ObjectCharacteristics {

    private BigInteger compositionLevel;

    private Fixity fixity;

    private long size;

    private Format format;

    private CreatingApplication creatingApplication;

    public ObjectCharacteristics(ObjectCharacteristicsComplexType xmlElement) {
        this.compositionLevel = xmlElement.getCompositionLevel();
        this.fixity = new Fixity(XMLUtils.getFirstIfOnly(xmlElement.getFixity()));
        this.size = xmlElement.getSize();
        this.format = new Format(XMLUtils.getFirstIfOnly(xmlElement.getFormat()));
        this.creatingApplication = new CreatingApplication(XMLUtils.getFirstIfOnly(xmlElement.getCreatingApplication()));
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Fixity {

        private String messageDigestAlgorithm;
        private String messageDigest;
        private String messageDigestOriginator;

        public Fixity(FixityComplexType xmlElement) {
            this.messageDigestAlgorithm = xmlElement.getMessageDigestAlgorithm();
            this.messageDigest = xmlElement.getMessageDigest();
            this.messageDigestOriginator = xmlElement.getMessageDigestOriginator();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Format {

        private FormatDesignation designation;
        private FormatRegistry registry;

        public Format(FormatComplexType xmlElement) {
            List<JAXBElement<?>> content = xmlElement.getContent();

            for (JAXBElement<?> child : content) {
                if (child.getValue() instanceof FormatDesignationComplexType) {
                    this.designation = new FormatDesignation((FormatDesignationComplexType) child.getValue());
                } if (child.getValue() instanceof FormatRegistryComplexType) {
                    this.registry = new FormatRegistry((FormatRegistryComplexType) child.getValue());
                }
            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class FormatDesignation {
            private String formatName;
            private String formatVersion;

            public FormatDesignation(FormatDesignationComplexType xmlElement) {
                this.formatName = xmlElement.getFormatName();
                this.formatVersion = xmlElement.getFormatVersion();
            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class FormatRegistry {
            private String formatRegistryName;
            private String formatRegistryKey;

            public FormatRegistry(FormatRegistryComplexType xmlElement) {
                this.formatRegistryName = xmlElement.getFormatRegistryName();
                this.formatRegistryKey = xmlElement.getFormatRegistryKey();
            }
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreatingApplication {
        private String creatingApplicationName;
        private String creatingApplicationVersion;
        private String dateCreatedByApplication;

        public CreatingApplication(CreatingApplicationComplexType xmlElement) {
            List<JAXBElement<?>> content = xmlElement.getContent();

            for (JAXBElement<?> child : content) {
                String tagName = child.getName().getLocalPart();

                switch (tagName) {
                    case "creatingApplicationName":
                        this.creatingApplicationName = child.getValue().toString();
                        break;
                    case "creatingApplicationVersion":
                        this.creatingApplicationVersion = child.getValue().toString();
                        break;
                    case "dateCreatedByApplication":
                        this.dateCreatedByApplication = child.getValue().toString();
                        break;
                }
            }
        }
    }
}

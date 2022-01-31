package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.mix;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.mets.MetsElement;
import gov.loc.mix.v20.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MetsMixElement extends MetsElement {

    private BasicDigitalInformation basicDigitalInformation;

    private BasicImageInformation basicImageInformation;

    private ImageCaptureMetadata imageCaptureMetadata;

    private ImageAssessmentMetadata imageAssessmentMetadata;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class BasicDigitalInformation {

        private List<Compression> compression;

        public BasicDigitalInformation(BasicDigitalObjectInformationType xmlElement) {
            if (xmlElement.getCompression() != null) {
                this.compression = xmlElement.getCompression()
                        .stream()
                        .map(Compression::new)
                        .collect(Collectors.toList());
            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class Compression {
            private String compressionScheme;

            public Compression(BasicDigitalObjectInformationType.Compression compression) {
                if (compression.getCompressionScheme() != null) {
                    this.compressionScheme = compression.getCompressionScheme().getValue();
                }
            }
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class BasicImageInformation {

        private BasicImageCharacteristics basicImageCharacteristics;

        public BasicImageInformation(BasicImageInformationType xmlElement) {
            if (xmlElement.getBasicImageCharacteristics() != null) {
                this.basicImageCharacteristics = new BasicImageCharacteristics(xmlElement.getBasicImageCharacteristics());
            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class BasicImageCharacteristics {
            private BigInteger imageWidth;
            private BigInteger imageHeight;
            private PhotometricInterpretation photometricInterpretation;

            public BasicImageCharacteristics(BasicImageInformationType.BasicImageCharacteristics xmlElement) {
                this.imageWidth = xmlElement.getImageWidth().getValue();
                this.imageHeight = xmlElement.getImageHeight().getValue();
                this.photometricInterpretation = new PhotometricInterpretation(xmlElement.getPhotometricInterpretation());
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class PhotometricInterpretation {
                private String colorSpace;

                public PhotometricInterpretation(BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation
                                                 photometricInterpretation) {
                    this.colorSpace = photometricInterpretation.getColorSpace().getValue();
                }
            }
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ImageCaptureMetadata {

        private GeneralCaptureInformation generalCaptureInformation;

        private ScannerCapture scannerCapture;

        private DigitalCameraCapture digitalCameraCapture;

        public ImageCaptureMetadata(ImageCaptureMetadataType xmlElement) {
            if (xmlElement.getGeneralCaptureInformation() != null) {
                this.generalCaptureInformation = new GeneralCaptureInformation(xmlElement.getGeneralCaptureInformation());
            }

            if (xmlElement.getScannerCapture() != null) {
                this.scannerCapture = new ScannerCapture(xmlElement.getScannerCapture());
            }

            if (xmlElement.getDigitalCameraCapture() != null) {
                this.digitalCameraCapture = new DigitalCameraCapture(xmlElement.getDigitalCameraCapture());
            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class GeneralCaptureInformation {
            private String dateTimeCreated;
            private List<String> imageProducers;
            private String captureDevice;

            public GeneralCaptureInformation(ImageCaptureMetadataType.GeneralCaptureInformation xmlElement) {
                this.dateTimeCreated = xmlElement.getDateTimeCreated().getValue();
                this.imageProducers = xmlElement.getImageProducer().stream().map(StringType::getValue).collect(Collectors.toList());
                this.captureDevice = xmlElement.getCaptureDevice().getValue().value();
            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class ScannerCapture {
            private String scannerManufacturer;
            private ScannerModel scannerModel;
            private MaximumOpticalResolution maximumOpticalResolution;
            private String scannerSensor;
            private ScanningSystemSoftware scanningSystemSoftware;

            public ScannerCapture(ImageCaptureMetadataType.ScannerCapture xmlElement) {
                if (xmlElement.getScannerManufacturer() != null) {
                    this.scannerManufacturer = xmlElement.getScannerManufacturer().getValue();
                }

                if (xmlElement.getScannerSensor() != null) {
                    this.scannerSensor = xmlElement.getScannerSensor().getValue().value();
                }

                if (xmlElement.getScannerModel() != null) {
                    this.scannerModel = new ScannerModel(xmlElement.getScannerModel());
                }

                if (xmlElement.getMaximumOpticalResolution() != null) {
                    this.maximumOpticalResolution = new MaximumOpticalResolution(xmlElement.getMaximumOpticalResolution());
                }

                if (xmlElement.getScanningSystemSoftware() != null) {
                    this.scanningSystemSoftware = new ScanningSystemSoftware(xmlElement.getScanningSystemSoftware());
                }
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class ScannerModel {
                private String scannerModelName;
                private String scannerModelNumber;
                private String scannerModelSerialNo;

                public ScannerModel(ImageCaptureMetadataType.ScannerCapture.ScannerModel xmlElement) {
                    if (xmlElement.getScannerModelName() != null) {
                        this.scannerModelName = xmlElement.getScannerModelName().getValue();
                    }

                    if (xmlElement.getScannerModelNumber() != null) {
                        this.scannerModelNumber = xmlElement.getScannerModelNumber().getValue();
                    }

                    if (xmlElement.getScannerModelSerialNo() != null) {
                        this.scannerModelSerialNo = xmlElement.getScannerModelSerialNo().getValue();
                    }
                }

            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class MaximumOpticalResolution {
                private BigInteger xOpticalResolution;
                private BigInteger yOpticalResolution;
                private String unit;

                public MaximumOpticalResolution(ImageCaptureMetadataType.ScannerCapture.MaximumOpticalResolution xmlElement) {
                    if (xmlElement.getXOpticalResolution() != null) {
                        this.xOpticalResolution = xmlElement.getXOpticalResolution().getValue();
                    }

                    if (xmlElement.getYOpticalResolution() != null) {
                        this.yOpticalResolution = xmlElement.getYOpticalResolution().getValue();
                    }

                    if (xmlElement.getOpticalResolutionUnit() != null) {
                        this.unit = xmlElement.getOpticalResolutionUnit().getValue().value();
                    }
                }
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class ScanningSystemSoftware {
                private String scanningSystemSoftwareName;
                private String scanningSystemSoftwareVersionNo;

                public ScanningSystemSoftware(ImageCaptureMetadataType.ScannerCapture.ScanningSystemSoftware xmlElement) {
                    if (xmlElement.getScanningSoftwareName() != null) {
                        this.scanningSystemSoftwareName = xmlElement.getScanningSoftwareName().getValue();
                    }

                    if (xmlElement.getScanningSoftwareVersionNo() != null) {
                        this.scanningSystemSoftwareVersionNo = xmlElement.getScanningSoftwareVersionNo().getValue();
                    }
                }
            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class DigitalCameraCapture {
            private String digitalCameraManufacturer;
            private DigitalCameraModel digitalCameraModel;
            private String digitalCameraSensor;
            private DigitalCameraCaptureSettings digitalCameraCaptureSettings;

            public DigitalCameraCapture(ImageCaptureMetadataType.DigitalCameraCapture xmlElement) {
                if (xmlElement.getDigitalCameraManufacturer() != null) {
                    this.digitalCameraManufacturer = xmlElement.getDigitalCameraManufacturer().getValue();
                }

                if (xmlElement.getDigitalCameraModel() != null) {
                    this.digitalCameraModel = new DigitalCameraModel(xmlElement.getDigitalCameraModel());
                }

                if (xmlElement.getCameraSensor() != null) {
                    this.digitalCameraSensor = xmlElement.getCameraSensor().getValue().value();
                }

                if (xmlElement.getCameraCaptureSettings() != null) {
                    this.digitalCameraCaptureSettings = new DigitalCameraCaptureSettings(xmlElement.getCameraCaptureSettings());
                }
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class DigitalCameraModel {
                private String digitalCameraModelName;
                private String digitalCameraModelNumber;
                private String digitalCameraModelSerialNo;

                public DigitalCameraModel(ImageCaptureMetadataType.DigitalCameraCapture.DigitalCameraModel xmlElement) {
                    if (xmlElement.getDigitalCameraModelName() != null) {
                        this.digitalCameraModelName = xmlElement.getDigitalCameraModelName().getValue();
                    }

                    if (xmlElement.getDigitalCameraModelNumber() != null) {
                        this.digitalCameraModelNumber = xmlElement.getDigitalCameraModelNumber().getValue();
                    }

                    if (xmlElement.getDigitalCameraModelSerialNo() != null) {
                        this.digitalCameraModelSerialNo = xmlElement.getDigitalCameraModelSerialNo().getValue();
                    }
                }
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class DigitalCameraCaptureSettings {
                private ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData imageData;

                public DigitalCameraCaptureSettings(ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings xmlElement) {
                    this.imageData = xmlElement.getImageData();
                }
            }
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ImageAssessmentMetadata {
        private ImageAssessmentMetadataType.SpatialMetrics spatialMetrics;
        private ImageAssessmentMetadataType.ImageColorEncoding imageColorEncoding;

        public ImageAssessmentMetadata(ImageAssessmentMetadataType xmlElement) {
            this.spatialMetrics = xmlElement.getSpatialMetrics();
            this.imageColorEncoding = xmlElement.getImageColorEncoding();
        }
    }
}

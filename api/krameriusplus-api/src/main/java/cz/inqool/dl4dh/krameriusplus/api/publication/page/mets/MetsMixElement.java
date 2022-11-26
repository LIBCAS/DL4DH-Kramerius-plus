package cz.inqool.dl4dh.krameriusplus.api.publication.page.mets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

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

        @Getter
        @Setter
        @NoArgsConstructor
        public static class Compression {
            private String compressionScheme;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class BasicImageInformation {

        private BasicImageCharacteristics basicImageCharacteristics;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class BasicImageCharacteristics {
            private BigInteger imageWidth;
            private BigInteger imageHeight;
            private PhotometricInterpretation photometricInterpretation;

            @Getter
            @Setter
            @NoArgsConstructor
            public static class PhotometricInterpretation {
                private String colorSpace;
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

        @Getter
        @Setter
        @NoArgsConstructor
        public static class GeneralCaptureInformation {
            private String dateTimeCreated;
            private List<String> imageProducers;
            private String captureDevice;
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

            @Getter
            @Setter
            @NoArgsConstructor
            public static class ScannerModel {
                private String scannerModelName;
                private String scannerModelNumber;
                private String scannerModelSerialNo;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class MaximumOpticalResolution {
                private BigInteger xOpticalResolution;
                private BigInteger yOpticalResolution;
                private String unit;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class ScanningSystemSoftware {
                private String scanningSystemSoftwareName;
                private String scanningSystemSoftwareVersionNo;
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

            @Getter
            @Setter
            @NoArgsConstructor
            public static class DigitalCameraModel {
                private String digitalCameraModelName;
                private String digitalCameraModelNumber;
                private String digitalCameraModelSerialNo;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class DigitalCameraCaptureSettings {
//                private ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData imageData;
                //TODO: map to own data structure
            }
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ImageAssessmentMetadata {
//        private ImageAssessmentMetadataType.SpatialMetrics spatialMetrics;
//        private ImageAssessmentMetadataType.ImageColorEncoding imageColorEncoding;
        // TODO: map to own data structure
    }
}

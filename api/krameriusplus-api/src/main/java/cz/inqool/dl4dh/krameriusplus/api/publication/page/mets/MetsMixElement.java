package cz.inqool.dl4dh.krameriusplus.api.publication.page.mets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MetsMixElement extends MetsElement {

    private BasicDigitalObjectInformation basicDigitalObjectInformation;

    private BasicImageInformation basicImageInformation;

    private ImageCaptureMetadata imageCaptureMetadata;

    private ImageAssessmentMetadata imageAssessmentMetadata;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasicDigitalObjectInformation {

        private List<Compression> compression;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Compression {
            private String compressionScheme;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasicImageInformation {

        private BasicImageCharacteristics basicImageCharacteristics;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class BasicImageCharacteristics {
            private BigInteger imageWidth;
            private BigInteger imageHeight;
            private PhotometricInterpretation photometricInterpretation;

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class PhotometricInterpretation {
                private String colorSpace;
            }
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageCaptureMetadata {

        private GeneralCaptureInformation generalCaptureInformation;

        private ScannerCapture scannerCapture;

        private DigitalCameraCapture digitalCameraCapture;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class GeneralCaptureInformation {
            private String dateTimeCreated;
            private List<String> imageProducers;
            private String captureDevice;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ScannerCapture {
            private String scannerManufacturer;
            private ScannerModel scannerModel;
            private MaximumOpticalResolution maximumOpticalResolution;
            private String scannerSensor;
            private ScanningSystemSoftware scanningSystemSoftware;

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class ScannerModel {
                private String scannerModelName;
                private String scannerModelNumber;
                private String scannerModelSerialNo;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class MaximumOpticalResolution {
                private BigInteger xOpticalResolution;
                private BigInteger yOpticalResolution;
                private String unit;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class ScanningSystemSoftware {
                private String scanningSoftwareName;
                private String scanningSoftwareVersionNo;
            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class DigitalCameraCapture {
            private String digitalCameraManufacturer;
            private DigitalCameraModel digitalCameraModel;
            private String digitalCameraSensor;
            private DigitalCameraCaptureSettings digitalCameraCaptureSettings;

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class DigitalCameraModel {
                private String digitalCameraModelName;
                private String digitalCameraModelNumber;
                private String digitalCameraModelSerialNo;
            }

            @Getter
            @Setter
            public static class DigitalCameraCaptureSettings {

//                private ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData imageData;

            }
        }
    }

    @Getter
    @Setter
    public static class ImageAssessmentMetadata {
        private SpatialMetrics spatialMetrics;
        private ImageColorEncoding imageColorEncoding;

        @Getter
        @Setter
        public static class SpatialMetrics {
            private String samplingFrequencyUnit;
            private String samplingFrequencyPlane;
            private SamplingFrequency xSamplingFrequency;
            private SamplingFrequency ySamplingFrequency;

            @Getter
            @Setter
            public static class SamplingFrequency {
                private String numerator;
                private String denominator;
            }
        }

        @Getter
        @Setter
        public static class ImageColorEncoding {
            private String samplesPerPixel;
            private BitsPerSample bitsPerSample;

            @Getter
            @Setter
            public static class BitsPerSample {
                private List<String> bitsPerSampleValues = new ArrayList<>();
                private String bitsPerSampleUnit;
            }
        }
    }
}

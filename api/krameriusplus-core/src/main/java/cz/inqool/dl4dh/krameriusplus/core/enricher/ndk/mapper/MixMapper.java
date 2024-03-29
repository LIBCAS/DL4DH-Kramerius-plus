package cz.inqool.dl4dh.krameriusplus.core.enricher.ndk.mapper;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.BasicDigitalObjectInformation;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.BasicDigitalObjectInformation.Compression;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.BasicImageInformation.BasicImageCharacteristics;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.BasicImageInformation.BasicImageCharacteristics.PhotometricInterpretation;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.ImageAssessmentMetadata;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.ImageCaptureMetadata;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.ImageCaptureMetadata.DigitalCameraCapture;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.ImageCaptureMetadata.DigitalCameraCapture.DigitalCameraModel;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.ImageCaptureMetadata.GeneralCaptureInformation;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.ImageCaptureMetadata.ScannerCapture;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.ImageCaptureMetadata.ScannerCapture.MaximumOpticalResolution;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.ImageCaptureMetadata.ScannerCapture.ScannerModel;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.ImageCaptureMetadata.ScannerCapture.ScanningSystemSoftware;
import cz.inqool.dl4dh.ndk.mix.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface MixMapper extends MetsMapperBase {

    MetsMixElement convert(MixType xmlElement);

    ImageAssessmentMetadata map(ImageAssessmentMetadataType xmlElement);

    @Mappings(value =  {
            @Mapping(target = "samplingFrequencyUnit", source = "samplingFrequencyUnit.value"),
            @Mapping(target = "samplingFrequencyPlane", source = "samplingFrequencyPlane.value")
    })
    ImageAssessmentMetadata.SpatialMetrics map(ImageAssessmentMetadataType.SpatialMetrics xmlElement);

    @Mappings(value = {
            @Mapping(target = "bitsPerSample.bitsPerSampleUnit", source = "bitsPerSample.bitsPerSampleUnit.value"),
            @Mapping(target = "bitsPerSample.bitsPerSampleValues", source = "bitsPerSample.bitsPerSampleValue")
    })
    ImageAssessmentMetadata.ImageColorEncoding map(ImageAssessmentMetadataType.ImageColorEncoding xmlElement);

    @Mappings(value = {
            @Mapping(target = "subjectDistance", source = "subjectDistance.distance.value"),
            @Mapping(target = "meteringMode", source = "meteringMode.value"),
            @Mapping(target = "lightSource", source = "lightSource.value"),
            @Mapping(target = "flash", source = "flash.value"),
            @Mapping(target = "focalLength", source = "focalLength.value"),
            @Mapping(target = "backLight", source = "backLight.value"),
            @Mapping(target = "sensingMethod", source = "sensingMethod.value"),
            @Mapping(target = "cfaPattern", source = "cfaPattern.value"),
            @Mapping(target = "autoFocus", source = "autoFocus.value")
    })
    DigitalCameraCapture.CameraCaptureSettings.ImageData map(ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData xmlElement);

    DigitalCameraModel map(ImageCaptureMetadataType.DigitalCameraCapture.DigitalCameraModel xmlElement);

    @Mappings({
            @Mapping(target = "digitalCameraSensor", source = "cameraSensor.value"),
            @Mapping(target = "digitalCameraCaptureSettings", source = "cameraCaptureSettings")
    })
    DigitalCameraCapture map(ImageCaptureMetadataType.DigitalCameraCapture xmlElement);

    ScanningSystemSoftware map(ImageCaptureMetadataType.ScannerCapture.ScanningSystemSoftware xmlElement);

    @Mappings({
            @Mapping(target = "unit", source = "opticalResolutionUnit.value")
    })
    MaximumOpticalResolution map(ImageCaptureMetadataType.ScannerCapture.MaximumOpticalResolution xmlElement);

    ScannerModel map(ImageCaptureMetadataType.ScannerCapture.ScannerModel xmlElement);

    @Mappings({
            @Mapping(target = "scannerSensor", source = "scannerSensor.value")
    })
    ScannerCapture map(ImageCaptureMetadataType.ScannerCapture xmlElement);

    @Mappings({
            @Mapping(target = "imageProducers", source = "imageProducer"),
            @Mapping(target = "captureDevice", source = "captureDevice.value")
    })
    GeneralCaptureInformation map(ImageCaptureMetadataType.GeneralCaptureInformation xmlElement);

    ImageCaptureMetadata map(ImageCaptureMetadataType xmlElement);

    PhotometricInterpretation map(BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation xmlElement);

    BasicImageCharacteristics map(BasicImageCharacteristics xmlElement);

    Compression map(BasicDigitalObjectInformationType.Compression xmlElement);

    BasicDigitalObjectInformation map(BasicDigitalObjectInformationType xmlElement);
}

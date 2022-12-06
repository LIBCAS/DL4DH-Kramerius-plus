package cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk.mapper;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.BasicDigitalInformation;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.BasicDigitalInformation.Compression;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.BasicImageInformation.BasicImageCharacteristics;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.BasicImageInformation.BasicImageCharacteristics.PhotometricInterpretation;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.ImageAssessmentMetadata;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.ImageCaptureMetadata;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.ImageCaptureMetadata.DigitalCameraCapture;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsMixElement.ImageCaptureMetadata.DigitalCameraCapture.DigitalCameraCaptureSettings;
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

    DigitalCameraCaptureSettings map(ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings xmlElement);

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

    BasicDigitalInformation map(BasicDigitalObjectInformationType xmlElement);
}

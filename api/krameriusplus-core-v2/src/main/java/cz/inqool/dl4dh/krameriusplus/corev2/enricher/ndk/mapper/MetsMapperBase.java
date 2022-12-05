package cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk.mapper;

import cz.inqool.dl4dh.ndk.mix.PositiveIntegerType;
import cz.inqool.dl4dh.ndk.mix.StringType;
import cz.inqool.dl4dh.ndk.mix.TypeOfDateType;

import java.math.BigInteger;
import java.util.List;

public interface MetsMapperBase {

    default BigInteger map(PositiveIntegerType xmlElement) {
        return xmlElement.getValue();
    }

    default String map(StringType stringType) {
        return stringType.getValue();
    }

    default String map(TypeOfDateType dateType) {
        return dateType.getValue();
    }

    List<String> mapStringTypes(List<StringType> xmlElement);
}

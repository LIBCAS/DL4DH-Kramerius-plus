package cz.inqool.dl4dh.krameriusplus.domain.entity.paradata;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class NameTagParadata extends Paradata {

    private String model;

    @Override
    public ExternalServiceType getExternalServiceType() {
        return ExternalServiceType.NAME_TAG;
    }
}
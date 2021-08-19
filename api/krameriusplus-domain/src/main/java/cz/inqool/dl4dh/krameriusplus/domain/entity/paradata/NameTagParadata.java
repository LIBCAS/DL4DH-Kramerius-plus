package cz.inqool.dl4dh.krameriusplus.domain.entity.paradata;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class NameTagParadata extends Paradata {

    private final String model;

    //todo: add user, who called the method
    public NameTagParadata(ExternalServiceType service, String model) {
        super(service);
        this.model = model;
    }
}

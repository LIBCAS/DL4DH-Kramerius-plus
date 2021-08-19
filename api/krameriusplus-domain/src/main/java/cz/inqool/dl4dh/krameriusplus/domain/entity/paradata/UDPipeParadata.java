package cz.inqool.dl4dh.krameriusplus.domain.entity.paradata;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class UDPipeParadata extends Paradata {

    private final String model;

    //todo: add user, who called the method
    public UDPipeParadata(ExternalServiceType service, String model) {
        super(service);
        this.model = model;
    }
}

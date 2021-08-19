package cz.inqool.dl4dh.krameriusplus.paradata;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class NameTagParadata extends Paradata {

    private final String model;

    //todo: add user, who called the method
    public NameTagParadata(ExternalService service, String model) {
        super(service);
        this.model = model;
    }
}

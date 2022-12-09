package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.SUPPLEMENT;


@Getter
@Setter
@TypeAlias(SUPPLEMENT)
public class Supplement extends Publication {
    @Override
    public String getModel() {
        return SUPPLEMENT;
    }
}

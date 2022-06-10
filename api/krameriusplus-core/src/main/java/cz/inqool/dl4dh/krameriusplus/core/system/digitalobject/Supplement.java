package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import static cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.KrameriusModel.SUPPLEMENT;

@Getter
@Setter
@TypeAlias(SUPPLEMENT)
@Document(collection = "publications")
public class Supplement extends Publication {

    @Override
    public String getModel() {
        return SUPPLEMENT;
    }
}

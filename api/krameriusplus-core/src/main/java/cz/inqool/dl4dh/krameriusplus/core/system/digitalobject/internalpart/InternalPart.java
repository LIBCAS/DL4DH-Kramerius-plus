package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.internalpart;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import static cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.KrameriusModel.INTERNAL_PART;

@Document(collection = "internalpart")
@TypeAlias(INTERNAL_PART)
public class InternalPart extends DigitalObject {
    @Override
    public String getModel() {
        return INTERNAL_PART;
    }
}

package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.internalpart;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.KrameriusModel;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "internalpart")
@TypeAlias(KrameriusModel.INTERNAL_PART)
public class InternalPart extends DigitalObject {
}

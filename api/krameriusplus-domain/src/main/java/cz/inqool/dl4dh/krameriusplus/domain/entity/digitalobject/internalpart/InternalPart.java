package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.internalpart;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.DigitalObject;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import static cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.KrameriusModel.INTERNAL_PART;

@Document(collection = "internalpart")
@TypeAlias(INTERNAL_PART)
public class InternalPart extends DigitalObject {
}

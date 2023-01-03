package cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication;

import cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.api.publication.object.InternalPartDto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.dto.DigitalObjectMapperVisitor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.KrameriusModelName.INTERNAL_PART;


@Getter
@Setter
@TypeAlias(INTERNAL_PART)
public class InternalPart extends Publication {

    private String pageRange;

    private String partTitle;

    private String partType;

    private String pageNumber;

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.INTERNAL_PART;
    }

    @Override
    public InternalPartDto accept(DigitalObjectMapperVisitor visitor) {
        return visitor.toDto(this);
    }
}

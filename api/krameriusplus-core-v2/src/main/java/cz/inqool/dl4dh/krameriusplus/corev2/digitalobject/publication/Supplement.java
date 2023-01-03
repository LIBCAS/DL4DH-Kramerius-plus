package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication;

import cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.api.publication.object.SupplementDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.DigitalObjectMapperVisitor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.KrameriusModelName.SUPPLEMENT;


@Getter
@Setter
@TypeAlias(SUPPLEMENT)
public class Supplement extends Publication {

    private String date;

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.SUPPLEMENT;
    }

    @Override
    public SupplementDto accept(DigitalObjectMapperVisitor visitor) {
        return visitor.toDto(this);
    }
}

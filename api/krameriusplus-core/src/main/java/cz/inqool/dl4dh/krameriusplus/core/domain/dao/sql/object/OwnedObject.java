package cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object;


import cz.inqool.dl4dh.krameriusplus.core.domain.security.user.KrameriusUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class OwnedObject extends DatedObject {

    @ManyToOne
    private KrameriusUser owner;
}

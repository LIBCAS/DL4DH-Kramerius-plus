package cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.security.user.KrameriusUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class OwnedObjectDto extends DatedObjectDto {

    protected KrameriusUser owner;
}
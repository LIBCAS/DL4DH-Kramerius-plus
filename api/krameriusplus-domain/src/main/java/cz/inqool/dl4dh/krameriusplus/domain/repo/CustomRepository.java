package cz.inqool.dl4dh.krameriusplus.domain.repo;

import cz.inqool.dl4dh.krameriusplus.domain.entity.DomainObject;
import cz.inqool.dl4dh.krameriusplus.domain.params.Params;

import java.util.List;

public interface CustomRepository<T extends DomainObject> {

    List<T> list(Params params);
}

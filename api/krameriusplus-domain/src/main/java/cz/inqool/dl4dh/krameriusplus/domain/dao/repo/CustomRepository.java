package cz.inqool.dl4dh.krameriusplus.domain.dao.repo;

import cz.inqool.dl4dh.krameriusplus.domain.dao.params.Params;
import cz.inqool.dl4dh.krameriusplus.domain.entity.DomainObject;

import java.util.List;

public interface CustomRepository<T extends DomainObject> {

    List<T> list(Params params);
}

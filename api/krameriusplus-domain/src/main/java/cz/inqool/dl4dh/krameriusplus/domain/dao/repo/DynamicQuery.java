package cz.inqool.dl4dh.krameriusplus.domain.dao.repo;

import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.filter.Params;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class DynamicQuery {

    private Params projectionParams;
}

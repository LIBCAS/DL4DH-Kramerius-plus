package cz.inqool.dl4dh.krameriusplus.domain.dao.repo;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Repository
public interface PageRepositoryCustom {

    List<Page> listWithProjection(DynamicQuery query);
}

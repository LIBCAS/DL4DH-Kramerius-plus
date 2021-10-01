package cz.inqool.dl4dh.krameriusplus.domain.entity;

/**
 * Type interface for entities that have a parent associated
 *
 * @author Norbert Bodnar
 */
public interface ParentAware {

    void setParentId(String id);

    Integer getIndex();

    void setIndex(Integer index);
}

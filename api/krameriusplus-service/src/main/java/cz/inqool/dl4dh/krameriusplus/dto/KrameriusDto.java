package cz.inqool.dl4dh.krameriusplus.dto;

/**
 * Interface that every dto class that implements a model from Kramerius should implement.
 *
 * @author Norbert Bodnar
 */
public interface KrameriusDto<T> {

    T toEntity();
}

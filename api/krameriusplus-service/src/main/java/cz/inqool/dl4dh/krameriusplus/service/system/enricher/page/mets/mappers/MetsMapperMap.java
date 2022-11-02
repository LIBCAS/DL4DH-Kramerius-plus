package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.mappers;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.mets.MetsElement;

import java.util.HashMap;

/**
 * Helper Map implementation to ensure same compile time types in key and value's generic types
 */
public class MetsMapperMap extends HashMap<Class<?>, MetsMapper<?>> {

    public <S extends MetsElement> MetsMapper<S> getMapper(Class<S> clazz) {
        return (MetsMapper<S>) get(clazz);
    }

    public void put(MetsMapper<?> mapper) {
        put(mapper.supports(), mapper);
    }

}

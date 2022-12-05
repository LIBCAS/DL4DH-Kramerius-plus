package cz.inqool.dl4dh.krameriusplus.corev2.enricher.ndk.converter;

import cz.inqool.dl4dh.krameriusplus.api.publication.page.mets.MetsElement;

import java.util.HashMap;

/**
 * Helper Map implementation to ensure same compile time types in key and value's generic types
 */
public class MetsConverterMap extends HashMap<Class<?>, MetsConverter<?>> {

    public <S extends MetsElement> MetsConverter<S> getMapper(Class<S> clazz) {
        return (MetsConverter<S>) get(clazz);
    }

    public void put(MetsConverter<?> mapper) {
        put(mapper.supports(), mapper);
    }

}

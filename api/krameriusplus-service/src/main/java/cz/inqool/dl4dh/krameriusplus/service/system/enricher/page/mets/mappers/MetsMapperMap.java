package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.mappers;

import java.util.HashMap;
public class MetsMapperMap extends HashMap<Class<?>, MetsMapper<?>> {

    public <S> MetsMapper<S> getMapper(Class<S> clazz) {
        return (MetsMapper<S>) get(clazz);
    }

    public void put(MetsMapper<?> mapper) {
        put(mapper.supports(), mapper);
    }

}

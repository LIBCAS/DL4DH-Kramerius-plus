package cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception;

import lombok.Getter;

import static cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.MissingObjectException.MissingObjectErrorCode.NO_ITEM;

@Getter
public class MissingObjectException extends GeneralException {

    private final Class<?> clazz;

    private final String objectId;

    public MissingObjectException(Class<?> clazz, String objectId) {
        super(NO_ITEM, LogLevel.INFO);
        this.clazz = clazz;
        this.objectId = objectId;
    }

    @Override
    public String toString() {
        return objectId == null ?
                "MissingObjectException{" +
                        "clazz=" + clazz +
                        ", args='" + getArgs() +
                        "'}"
                :
                "MissingObjectException{" +
                        "clazz=" + clazz +
                        ", objectId='" + objectId +
                        "'}";
    }

    public enum MissingObjectErrorCode implements LogCode {
        NO_ITEM
    }

}

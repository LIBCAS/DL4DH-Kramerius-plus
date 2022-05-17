package cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao;


import cz.inqool.dl4dh.krameriusplus.core.domain.exception.GeneralException;

public class PersistenceException extends GeneralException {

    public PersistenceException(String message, PersistenceErrorCode errorCode, LogLevel errorLevel) {
        super(message, errorCode, errorLevel);
    }

    public enum PersistenceErrorCode implements LogCode {
        QUERY_DSL,
        DELETED_ENTITY
    }

}

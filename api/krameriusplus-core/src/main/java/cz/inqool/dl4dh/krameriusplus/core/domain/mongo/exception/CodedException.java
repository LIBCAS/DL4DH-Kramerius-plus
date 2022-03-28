package cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception;

/**
 * The {@code CodedException} interface should be implemented in exception classes to provide additional information (as
 * a predefined code) in error HTTP responses to the front-end for better debugging.
 *
 * @author Norbert Bodnar
 */
public interface CodedException {

    /**
     * Returns error code as an enum value.
     */
    Enum<? extends ExceptionCodeEnum> getErrorCode();

    /**
     * Class used to mark an enum class as a source for exception codes.
     */
    interface ExceptionCodeEnum {
    }
}

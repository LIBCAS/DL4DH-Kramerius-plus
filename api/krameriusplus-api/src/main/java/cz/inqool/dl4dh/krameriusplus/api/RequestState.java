package cz.inqool.dl4dh.krameriusplus.api;

import java.util.Collection;

public enum RequestState {
    CREATED,
    RUNNING,
    COMPLETED,
    FAILED,
    PARTIAL;

    public static RequestState from(Collection<RequestState> states) {
        if (states.stream().anyMatch(RUNNING::equals)) {
            return RequestState.RUNNING;
        }

        if (states.stream().allMatch(FAILED::equals)) {
            return RequestState.FAILED;
        }

        if (states.stream().allMatch(COMPLETED::equals)) {
            return RequestState.COMPLETED;
        }

        return RequestState.PARTIAL;
    }
}

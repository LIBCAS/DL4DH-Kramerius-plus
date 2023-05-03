package cz.inqool.dl4dh.krameriusplus.api;

import java.util.Collection;
import java.util.List;

public enum RequestState {
    CREATED,
    RUNNING,
    COMPLETED,
    FAILED,
    CANCELLED,
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

    public boolean isComplete() {
        return List.of(COMPLETED, FAILED, CANCELLED, PARTIAL).contains(this);
    }
}

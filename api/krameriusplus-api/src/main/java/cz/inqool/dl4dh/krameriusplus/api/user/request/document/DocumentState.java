package cz.inqool.dl4dh.krameriusplus.api.user.request.document;


import java.util.Set;

public enum DocumentState {

    WAITING,
    APPROVED,
    ENRICHED,
    OTHER;

    public Set<DocumentState> getTransitions() {
        switch (this) {
            case WAITING:
                return Set.of(APPROVED);
            case APPROVED:
                return Set.of(ENRICHED, OTHER);
            case ENRICHED:
                return Set.of(OTHER);
            case OTHER:
                return Set.of(APPROVED, ENRICHED);
        }

        throw new IllegalStateException("Unknown document state: " + this);
    }
}

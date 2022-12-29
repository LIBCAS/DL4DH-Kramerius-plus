package cz.inqool.dl4dh.krameriusplus.api.export;

public enum ExportState {
    CREATED,
    FAILED,
    SUCCESSFUL,
    PARTIAL;

    public boolean isIncomplete() {
        return this.equals(PARTIAL) || this.equals(FAILED);
    }
}

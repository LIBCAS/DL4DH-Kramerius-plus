package cz.inqool.dl4dh.krameriusplus.paradata;

import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

import static cz.inqool.dl4dh.krameriusplus.paradata.ExternalService.OCR;

@Getter
public class OCRParadata extends Paradata {

    private final LocalDate ocrPerformedDate;
    private final String creator;
    private final String softwareName;
    private final String version;

    public OCRParadata(LocalDate ocrPerformedDate, String creator, String softwareName, String version) {
        super(OCR);
        this.ocrPerformedDate = ocrPerformedDate;
        this.creator = creator;
        this.softwareName = softwareName;
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OCRParadata)) {
            return false;
        }
        OCRParadata other = (OCRParadata) o;
        if (!(other.canEqual(this))) {
            return false;
        }

        if (!Objects.equals(ocrPerformedDate, other.ocrPerformedDate)) {
            return false;
        }

        if (!Objects.equals(creator, other.creator)) {
            return false;
        }

        if (!Objects.equals(softwareName, other.softwareName)) {
            return false;
        }

        return Objects.equals(version, other.version);
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = (result * PRIME) + (ocrPerformedDate == null ? 43 : ocrPerformedDate.hashCode());
        result = (result * PRIME) + (creator == null ? 43 : creator.hashCode());
        result = (result * PRIME) + (softwareName == null ? 43 : softwareName.hashCode());
        result = (result * PRIME) + (version == null ? 43 : version.hashCode());

        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof OCRParadata;
    }
}

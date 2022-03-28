package cz.inqool.dl4dh.krameriusplus.core.jms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EnrichMessage {

    /**
     * Provide executionId if it is a job restart request
     */
    private Long executionId;

    private String publicationId;

    private Date timestamp;

    /**
     * Constructor for restarting job
     */
    public EnrichMessage(Long executionId) {
        this.executionId = executionId;
    }

    /**
     * Constructor for starting a new job
     */
    public EnrichMessage(String publicationId, Date timestamp) {
        this.publicationId = publicationId;
        this.timestamp = timestamp;
    }
}

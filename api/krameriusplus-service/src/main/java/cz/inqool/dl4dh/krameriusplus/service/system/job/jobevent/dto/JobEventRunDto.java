package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO used for communicating data with ActiveMQ Queue.
 */
@Getter
@Setter
public class JobEventRunDto {

    private String jobEventId;
}

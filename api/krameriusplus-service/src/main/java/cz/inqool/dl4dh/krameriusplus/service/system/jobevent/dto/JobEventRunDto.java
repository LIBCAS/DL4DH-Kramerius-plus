package cz.inqool.dl4dh.krameriusplus.service.system.jobevent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO used for communicating data with ActiveMQ Queue.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobEventRunDto {

    private String jobEventId;
}

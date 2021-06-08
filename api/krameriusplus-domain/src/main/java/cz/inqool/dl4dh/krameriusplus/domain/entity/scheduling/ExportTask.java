package cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document("tasks")
public class ExportTask {

    @Id
    @JsonIgnore
    private String id = java.util.UUID.randomUUID().toString();
}

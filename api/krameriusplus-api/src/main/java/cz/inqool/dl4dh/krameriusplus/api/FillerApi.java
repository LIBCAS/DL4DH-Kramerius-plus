package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.service.filler.FillerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Norbert Bodnar
 */
@RestController
@RequestMapping("api/filler")
public class FillerApi {

    private final FillerService fillerService;

    @Autowired
    public FillerApi(FillerService fillerService) {
        this.fillerService = fillerService;
    }

    @Operation(summary = "Enrich a publication with given PID",
            description = "This API enriches publications synchronously. It could cause response TimedOut on larger " +
                    "publications, only for TESTING purposes. To enrich a publication asynchronously, use the SchedulerApi"
    )
    @PostMapping(value = "/{pid}")
    public void enrichPublication(@PathVariable("pid") String pid) {
        fillerService.enrichPublication(pid);
//        throw new NotImplementedException("Logging to Tasks fails if called synchronously");
    }
}

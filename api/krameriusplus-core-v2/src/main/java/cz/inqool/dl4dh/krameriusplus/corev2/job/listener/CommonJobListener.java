package cz.inqool.dl4dh.krameriusplus.corev2.job.listener;

import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonJobListener implements KrameriusJobListener {

    private KrameriusJobInstanceService instanceService;

    @Override
    public void afterJob(KrameriusJobInstance krameriusJobInstance) {
        instanceService.updateStatus(krameriusJobInstance);
    }

    @Override
    public boolean supports(KrameriusJobInstance jobInstance) {
        return true;
    }

    @Autowired
    public void setInstanceService(KrameriusJobInstanceService instanceService) {
        this.instanceService = instanceService;
    }
}

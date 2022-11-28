package cz.inqool.dl4dh.krameriusplus.corev2.job.listener;

import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KrameriusJobListenerContainer {

    private List<KrameriusJobListener> listeners = new ArrayList<>();

    public void applyAfterJobListeners(KrameriusJobInstance job) {
        for (KrameriusJobListener listener : listeners) {
            if (listener.supports(job)) {
                listener.afterJob(job);
            }
        }
    }

    @Autowired
    public void setListeners(List<KrameriusJobListener> listeners) {
        this.listeners = listeners;
    }
}

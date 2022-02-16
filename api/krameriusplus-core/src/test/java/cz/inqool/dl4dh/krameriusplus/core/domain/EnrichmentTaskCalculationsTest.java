package cz.inqool.dl4dh.krameriusplus.core.domain;

import cz.inqool.dl4dh.krameriusplus.core.system.scheduling.EnrichmentTask;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnrichmentTaskCalculationsTest {

    @Test
    void percentDoneBeforeStart() {
        EnrichmentTask enrichmentTask = prepareEnrichmentTask();

        assertEquals(0, enrichmentTask.getPercentDone());
    }

    @Test
    void percentDoneOneSetUp() {
        EnrichmentTask enrichmentTask = prepareEnrichmentTask();
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(0).setTotalPages(10);

        assertEquals(0, enrichmentTask.getPercentDone());
    }

    @Test
    void percentDoneOneStarted() {
        EnrichmentTask enrichmentTask = prepareEnrichmentTask();
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(0).setTotalPages(10);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(0).setCurrentPage(5);

        assertEquals(0.13, enrichmentTask.getPercentDone());
    }

    @Test
    void percentDoneOneFinishedOneStarted() {
        EnrichmentTask enrichmentTask = prepareEnrichmentTask();
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(0).setTotalPages(10);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(0).setCurrentPage(10);

        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(1).setTotalPages(40);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(1).setCurrentPage(10);

        assertEquals(0.31, enrichmentTask.getPercentDone());
    }

    @Test
    void allDone() {
        EnrichmentTask enrichmentTask = prepareEnrichmentTask();

        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(0).setTotalPages(10);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(0).setCurrentPage(10);

        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(1).setTotalPages(40);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(1).setCurrentPage(40);

        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(2).setTotalPages(10);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(2).setCurrentPage(10);

        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(3).setTotalPages(12);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(3).setCurrentPage(12);

        assertEquals(1, enrichmentTask.getPercentDone());
    }

    @Test
    void volumeContainsPagesAsWellAsItems() {
        EnrichmentTask enrichmentTask = prepareEnrichmentTask();
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(0).setTotalPages(10);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(0).setCurrentPage(10);

        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(1).setTotalPages(40);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(1).setCurrentPage(10);

        enrichmentTask.getSubtask().getSubtasks().get(0).setTotalPages(10);
        enrichmentTask.getSubtask().getSubtasks().get(0).setCurrentPage(5);

        assertEquals(0.35, enrichmentTask.getPercentDone());
    }

    @Test
    void volumeContainsPagesAsWellAsItemsAllDone() {
        EnrichmentTask enrichmentTask = prepareEnrichmentTask();

        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(0).setTotalPages(10);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(0).setCurrentPage(10);

        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(1).setTotalPages(40);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(1).setCurrentPage(40);

        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(2).setTotalPages(10);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(2).setCurrentPage(10);

        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(3).setTotalPages(12);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(3).setCurrentPage(12);

        enrichmentTask.getSubtask().getSubtasks().get(0).setTotalPages(10);
        enrichmentTask.getSubtask().getSubtasks().get(0).setCurrentPage(10);

        assertEquals(1, enrichmentTask.getPercentDone());
    }

    @Test
    void volumeContainsPagesAsWellAsItemsAllSubtaskDone() {
        EnrichmentTask enrichmentTask = prepareEnrichmentTask();

        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(0).setTotalPages(10);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(0).setCurrentPage(10);

        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(1).setTotalPages(40);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(1).setCurrentPage(40);

        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(2).setTotalPages(10);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(2).setCurrentPage(10);

        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(3).setTotalPages(12);
        enrichmentTask.getSubtask().getSubtasks().get(0).getSubtasks().get(3).setCurrentPage(12);

        enrichmentTask.getSubtask().getSubtasks().get(0).setTotalPages(10);
        enrichmentTask.getSubtask().getSubtasks().get(0).setCurrentPage(0);

        assertEquals(0.8, enrichmentTask.getPercentDone());
    }

    @Test
    void stringPercentDone() {
        List<EnrichmentTask.EnrichmentSubTask> items = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            EnrichmentTask.EnrichmentSubTask item = new EnrichmentTask.EnrichmentSubTask("item" + i);
            if (i < 14) {
                item.setTotalPages(12);
                item.setCurrentPage(12);
            } else if (i == 14) {
                item.setTotalPages(12);
                item.setCurrentPage(7);
            }
            items.add(item);
        }

        EnrichmentTask.EnrichmentSubTask volume = new EnrichmentTask.EnrichmentSubTask("volume");
        volume.setSubtasks(items);

        EnrichmentTask.EnrichmentSubTask periodical = new EnrichmentTask.EnrichmentSubTask("periodical");
        periodical.setSubtasks(asList(volume));

        EnrichmentTask task = new EnrichmentTask();
        task.setSubtask(periodical);

        assertEquals("58%", task.getDone());
    }

    private EnrichmentTask prepareEnrichmentTask() {
        EnrichmentTask.EnrichmentSubTask item1 = new EnrichmentTask.EnrichmentSubTask("periodicalItem1");
        EnrichmentTask.EnrichmentSubTask item2 = new EnrichmentTask.EnrichmentSubTask("periodicalItem2");
        EnrichmentTask.EnrichmentSubTask item3 = new EnrichmentTask.EnrichmentSubTask("periodicalItem3");
        EnrichmentTask.EnrichmentSubTask item4 = new EnrichmentTask.EnrichmentSubTask("periodicalItem4");

        EnrichmentTask.EnrichmentSubTask volume = new EnrichmentTask.EnrichmentSubTask("volume");
        volume.setSubtasks(asList(item1, item2, item3, item4));

        EnrichmentTask enrichmentTask = new EnrichmentTask("periodical");
        enrichmentTask.getSubtask().getSubtasks().add(volume);

        return enrichmentTask;
    }
}

package cz.inqool.dl4dh.krameriusplus.corev2.job.config;

import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JobParametersMapWrapper {

    @Getter
    private final Map<String, Object> jobParametersMap = new HashMap<>();

    private void putDate(String key, Date date) {
        jobParametersMap.put(key, date);
    }

    private void putString(String key, String string) {
        jobParametersMap.put(key, string);
    }

    private void putLong(String key, Long aLong) {
        jobParametersMap.put(key, aLong);
    }

    private void putDouble(String key, Double aDouble) {
        jobParametersMap.put(key, aDouble);
    }

    private void putBoolean(String key, String bool) {
        jobParametersMap.put(key, Boolean.valueOf(bool));
    }
}

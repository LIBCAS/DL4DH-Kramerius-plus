package cz.inqool.dl4dh.krameriusplus.core.job.config;

import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JobParametersMapWrapper {

    @Getter
    private final Map<String, Object> jobParametersMap = new HashMap<>();

    public void putDate(String key, Date date) {
        jobParametersMap.put(key, date);
    }

    public void putString(String key, String string) {
        jobParametersMap.put(key, string);
    }

    public void putLong(String key, Long aLong) {
        jobParametersMap.put(key, aLong);
    }

    public void putDouble(String key, Double aDouble) {
        jobParametersMap.put(key, aDouble);
    }

    public void putBoolean(String key, Boolean bool) {
        jobParametersMap.put(key, Boolean.toString(bool));
    }
}

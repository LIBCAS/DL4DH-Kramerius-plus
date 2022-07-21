package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.monograph;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MonographTest {
    @Test
    public void TestMonographMultipleDonators() throws JsonProcessingException {
        String json = "{\"datanode\":false," +
                "\"context\":[[{\"pid\":\"uuid:21426150-9e46-11dc-a259-000d606f5dc6\",\"model\":\"monograph\"}]]," +
                "\"pid\":\"uuid:21426150-9e46-11dc-a259-000d606f5dc6\"," +
                "\"model\":\"monograph\"," +
                "\"handle\":{\"href\":\"https://kramerius5.nkp.cz/search/handle/uuid:21426150-9e46-11dc-a259-000d606f5dc6\"}," +
                "\"title\":\"Dějepispromládežčeskoslovanskounaškoláchnárodních\"," +
                "\"donator\":[\"donator:norway\",\"donator:k3tok5\"]," +
                "\"root_title\":\"Dějepispromládežčeskoslovanskounaškoláchnárodních\"," +
                "\"root_pid\":\"uuid:21426150-9e46-11dc-a259-000d606f5dc6\",\"policy\":\"public\"}";

        Monograph mono = new ObjectMapper()
                .readerFor(Monograph.class)
                .readValue(json);

        assertEquals("Dějepispromládežčeskoslovanskounaškoláchnárodních",
                mono.getTitle());

        assertEquals(List.of("donator:norway", "donator:k3tok5"),
                mono.getDonator());
    }

    @Test
    public void TestMonographSingleDonator() throws JsonProcessingException {
        String json = "{\"datanode\":false," +
                "\"context\":[[{\"pid\":\"uuid:21426150-9e46-11dc-a259-000d606f5dc6\",\"model\":\"monograph\"}]]," +
                "\"pid\":\"uuid:21426150-9e46-11dc-a259-000d606f5dc6\"," +
                "\"model\":\"monograph\"," +
                "\"handle\":{\"href\":\"https://kramerius5.nkp.cz/search/handle/uuid:21426150-9e46-11dc-a259-000d606f5dc6\"}," +
                "\"title\":\"Dějepispromládežčeskoslovanskounaškoláchnárodních\"," +
                "\"donator\":\"donator:k3tok5\"," +
                "\"root_title\":\"Dějepispromládežčeskoslovanskounaškoláchnárodních\"," +
                "\"root_pid\":\"uuid:21426150-9e46-11dc-a259-000d606f5dc6\",\"policy\":\"public\"}";

        Monograph mono = new ObjectMapper()
                .readerFor(Monograph.class)
                .readValue(json);

        assertEquals("Dějepispromládežčeskoslovanskounaškoláchnárodních",
                mono.getTitle());

        assertEquals(List.of("donator:k3tok5"),
                mono.getDonator());

    }

    @Test
    public void TestMonographNoDonators() throws JsonProcessingException {
        String json = "{\"datanode\":false," +
                "\"context\":[[{\"pid\":\"uuid:21426150-9e46-11dc-a259-000d606f5dc6\",\"model\":\"monograph\"}]]," +
                "\"pid\":\"uuid:21426150-9e46-11dc-a259-000d606f5dc6\"," +
                "\"model\":\"monograph\"," +
                "\"handle\":{\"href\":\"https://kramerius5.nkp.cz/search/handle/uuid:21426150-9e46-11dc-a259-000d606f5dc6\"}," +
                "\"title\":\"Dějepispromládežčeskoslovanskounaškoláchnárodních\"," +
                "\"donator\":[]," +
                "\"root_title\":\"Dějepispromládežčeskoslovanskounaškoláchnárodních\"," +
                "\"root_pid\":\"uuid:21426150-9e46-11dc-a259-000d606f5dc6\",\"policy\":\"public\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        Monograph monograph = objectMapper
                .readerFor(Monograph.class)
                .readValue(json);

        assertEquals("[]",
                objectMapper.writeValueAsString(monograph.getDonator()));
    }
}
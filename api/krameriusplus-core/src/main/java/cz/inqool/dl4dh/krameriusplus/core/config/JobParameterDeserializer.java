package cz.inqool.dl4dh.krameriusplus.core.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.batch.core.JobParameter;

import java.io.IOException;

public class JobParameterDeserializer extends StdDeserializer<JobParameter> {

    public JobParameterDeserializer() {
        super(JobParameter.class);
    }

    @Override
    public JobParameter deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        JobParameter.ParameterType parameterType = JobParameter.ParameterType.valueOf(node.get("parameterType").asText());
        boolean identifying = node.get("identifying").asBoolean();

        JsonNode parameter = node.get("parameter");
        switch (parameterType) {
            case STRING:
                String stringParameter = parameter.asText();
                return new JobParameter(stringParameter, identifying);
            case LONG:
                Long longParameter = parameter.asLong();
                return new JobParameter(longParameter, identifying);
            default:
                double doubleParameter = parameter.asDouble();
                return new JobParameter(doubleParameter, identifying);

                // TODO: add Date case
        }
    }
}


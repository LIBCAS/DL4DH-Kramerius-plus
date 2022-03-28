package cz.inqool.dl4dh.krameriusplus.core.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpoint;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;

@Slf4j
@Configuration
public class JmsListenersConfig implements JmsListenerConfigurer {

    public static final String ENRICHING_QUEUE = "enriching";

    public static final String EXPORT_QUEUE = "export";

    private Job enrichingJob;

    private Job jsonExportingJob;

    private JobService jobService;

    private MessageConverter messageConverter;

    private ObjectMapper objectMapper;

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        registrar.registerEndpoint(createEnrichingListener());
        registrar.registerEndpoint(createExportingListener());
    }

    private JmsListenerEndpoint createEnrichingListener() {
        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setId(ENRICHING_QUEUE);
        endpoint.setDestination(ENRICHING_QUEUE);
        endpoint.setMessageListener(message -> {
            log.trace("Message received {}", message);
            try {
                EnrichMessage enrichMessage = (EnrichMessage) messageConverter.fromMessage(message);

                if (enrichMessage.getExecutionId() != null) {
                    jobService.restartJob(enrichMessage.getExecutionId());
                } else {

                    JobParameters jobParameters = new JobParametersBuilder()
                            .addString("publicationId", enrichMessage.getPublicationId())
                            .addDate("timestamp", enrichMessage.getTimestamp())
                            .toJobParameters();

                    jobService.runJob(enrichingJob, jobParameters);
                }
            } catch (JMSException e) {
                log.error("Received Exception : " + e);
            }
        });

        return endpoint;
    }

    private JmsListenerEndpoint createExportingListener() {
        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setId(EXPORT_QUEUE);
        endpoint.setDestination(EXPORT_QUEUE);
        endpoint.setMessageListener(message -> {
            log.trace("Message received {}", message);
            try {
                ExportMessage exportMessage = (ExportMessage) messageConverter.fromMessage(message);

                JobParameters jobParameters = new JobParametersBuilder()
                        .addString("publicationId", exportMessage.getPublicationId())
                        .addString("publicationTitle", exportMessage.getPublicationTitle())
                        .addString("params", objectMapper.writeValueAsString(exportMessage.getParams()))
                        .toJobParameters();

                jobService.runJob(jsonExportingJob, jobParameters);
            } catch (JMSException e) {
                log.error("Received Exception : "+ e);
            } catch (JsonProcessingException exception) {
                log.error("Error converting Params to string");
            }
        });

        return endpoint;
    }

    @Autowired
    public void setEnrichingJob(Job enrichingJob) {
        this.enrichingJob = enrichingJob;
    }

    @Autowired
    public void setExportingJob(Job jsonExportingJob) {
        this.jsonExportingJob = jsonExportingJob;
    }

    @Autowired
    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setJobService(JobService jobService) {
        this.jobService = jobService;
    }
}

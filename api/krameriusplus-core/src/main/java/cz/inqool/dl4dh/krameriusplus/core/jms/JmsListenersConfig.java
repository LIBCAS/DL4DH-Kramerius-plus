package cz.inqool.dl4dh.krameriusplus.core.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpoint;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Slf4j
@Configuration
public class JmsListenersConfig implements JmsListenerConfigurer {

    public static final String ENRICHING_QUEUE = "enriching";

    public static final String EXPORT_QUEUE = "export";

    private JobLauncher jobLauncher;

    private Job enrichingJob;

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        registrar.registerEndpoint(createEnrichingListener());
    }

    private JmsListenerEndpoint createEnrichingListener() {
        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setId(ENRICHING_QUEUE);
        endpoint.setDestination(ENRICHING_QUEUE);
        endpoint.setMessageListener(message -> {
            log.trace("Message received {}", message);
            try {
                TextMessage textMessage = (TextMessage) message;

                String publicationId = textMessage.getText();

                JobParameters jobParameters = new JobParametersBuilder()
                        .addString("publicationId", publicationId)
                        .toJobParameters();

                jobLauncher.run(enrichingJob, jobParameters);
            } catch (JMSException e) {
                log.error("Received Exception : "+ e);
            } catch (JobInstanceAlreadyCompleteException |
                    JobExecutionAlreadyRunningException |
                    JobParametersInvalidException |
                    JobRestartException e) {
                e.printStackTrace(); //TODO
            }
        });

        return endpoint;
    }

    @Autowired
    public void setEnrichingJob(Job enrichingJob) {
        this.enrichingJob = enrichingJob;
    }

    @Autowired
    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }
}

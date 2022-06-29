package cz.inqool.dl4dh.krameriusplus.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQConnectionFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JmsConfig {

    private ObjectMapper objectMapper;

    private int minJmsListeners;

    private int maxJmsListeners;

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public ActiveMQConnectionFactoryCustomizer configureRedeliveryPolicy() {
        return connectionFactory ->
        {
            RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
            redeliveryPolicy.setInitialRedeliveryDelay(500);
            redeliveryPolicy.setMaximumRedeliveries(0);
            connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
        };
    }

    @Bean
    @Primary
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConcurrency(String.format("%d-%d", minJmsListeners, maxJmsListeners));
        factory.setConnectionFactory(connectionFactory);

        return factory;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setMinJmsListeners(@Value("${active-mq.messaging.min-listeners:5}") int minJmsListeners) {
        this.minJmsListeners = minJmsListeners;
    }

    @Autowired
    public void setMaxJmsListeners(@Value("${active-mq.messaging.max-listeners:10}") int maxJmsListeners) {
        this.maxJmsListeners = maxJmsListeners;
    }
}

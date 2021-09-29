package com.middleware.lab.jms;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class MessagingConfiguration {

    @Value("${lab.conf.queue.name}")
    private String ASYNC_QUEUE;
    @Value("${lab.activemq.broker-url}")
    private String brokerUrl;
    @Value("${lab.activemq.user}")
    String activemqUser;
    @Value("${lab.activemq.password}")
    String password;
    @Value("${lab.jms.cache.enabled}")
    boolean cacheEnabled;

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("user", "password", brokerUrl);
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public JmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory
                = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        //core poll size=4 threads and max poll size 16 threads
        factory.setConcurrency("4-16");
        return factory;
    }

}

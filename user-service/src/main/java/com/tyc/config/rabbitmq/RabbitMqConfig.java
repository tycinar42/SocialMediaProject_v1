package com.tyc.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${rabbitmq.exchange-user}")
    private String directExchange;

    @Value("${rabbitmq.userUpdateQueue}")
    private String userUpdateQueue;
    @Value("${rabbitmq.userUpdateBindingKey}")
    private String userUpdateBindingKey;

    @Value("${rabbitmq.userUpdateQueueElastic}")
    private String userUpdateQueueElastic;

//    @Value("${rabbitmq.userUpdateBindingKeyElastic}")
//    private String userUpdateBindingKeyElastic;
//
//    private String fanout = "fanout";

    @Bean
    DirectExchange exchangeUser() {
        return new DirectExchange(directExchange);
    }

//    @Bean
//    FanoutExchange exchange() {
//        return new FanoutExchange(fanout);
//    }
//
//    @Bean
//    Queue fanoutElasticQueue() {
//        return new Queue("elastic");
//    }

    //    @Bean
//    Queue fanoutAuthQueue() {
//        return new Queue("auth");
//    }
//
//    @Bean
//    Binding elasticBinding(Queue fanoutElasticQueue, FanoutExchange exchange) {
//        return BindingBuilder.bind(fanoutElasticQueue).to(exchange);
//    }
//
//    @Bean
//    Binding authBinding(Queue fanoutAuthQueue, FanoutExchange exchange) {
//        return BindingBuilder.bind(fanoutAuthQueue).to(exchange);
//    }
//
//
    @Bean
    Queue userUpdaeteQueue() {

        return new Queue(userUpdateQueue);
    }

    @Bean
    Queue userUpdaeteQueueElastic() {

        return new Queue(userUpdateQueueElastic);
    }

    @Bean
    public Binding bindingUpdateUser(final Queue userUpdaeteQueue, final DirectExchange exchangeUser) {

        return BindingBuilder.bind(userUpdaeteQueue).to(exchangeUser).with(userUpdateBindingKey);

    }

    @Bean
    public Binding bindingUpdateUserElastic(final Queue userUpdaeteQueueElastic, final DirectExchange exchangeUser) {

        return BindingBuilder.bind(userUpdaeteQueueElastic).to(exchangeUser).with(userUpdateBindingKey);

    }

}

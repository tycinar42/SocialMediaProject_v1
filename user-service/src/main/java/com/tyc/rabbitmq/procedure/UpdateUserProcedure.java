package com.tyc.rabbitmq.procedure;

import com.tyc.rabbitmq.model.UpdateUsernameEmailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserProcedure {
    @Value("${rabbitmq.exchange-user}")
    private String directExchange;


    @Value("${rabbitmq.userUpdateBindingKey}")
    private String userUpdateBindingKey;

    private final RabbitTemplate rabbitTemplate;


    public void sendUpdateUser(UpdateUsernameEmailModel model) {

        rabbitTemplate.convertAndSend(directExchange, userUpdateBindingKey, model);

    }

}

package com.tyc.rabbitmq.procuder;

import com.tyc.rabbitmq.model.ActivateReguestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivatedCodeProcedure {
    @Value("${rabbitmq.exchange-auth}")
    private String exchange;
    @Value("${rabbitmq.bindingKey}")
    private String bindingKeyActivatedCode;
    private final RabbitTemplate rabbitTemplate;


    public void sendActivatedCode(ActivateReguestDto dto) {

        rabbitTemplate.convertAndSend(exchange, bindingKeyActivatedCode, dto);
    }

}

package com.tyc.rabbitmq.consumer;

import com.tyc.rabbitmq.model.ActivateReguestDto;
import com.tyc.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivatedCodeConsumer {


    private final EmailSenderService emailSenderService;


    @RabbitListener(queues = "activated-queue")
    public void activatedMessage(ActivateReguestDto dto) {

        log.info("Activate: {}", dto.toString());
        emailSenderService.sendActivateCode(dto);

    }


}

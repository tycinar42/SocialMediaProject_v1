package com.tyc.service;

import com.tyc.rabbitmq.model.ActivateReguestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {


    private final JavaMailSender mailSender;


    public void sendActivateCode(ActivateReguestDto dto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("musty1406@gmail.com");
        mailMessage.setTo(dto.getEmail());
        mailMessage.setSubject("Aktivasyon Kodunuz");
        mailMessage.setText(dto.getActivatedCode());
        mailSender.send(mailMessage);

    }

}

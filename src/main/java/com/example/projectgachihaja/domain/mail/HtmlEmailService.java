package com.example.projectgachihaja.domain.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Component
@Async
@RequiredArgsConstructor
public class HtmlEmailService implements EmailService{

    private  final JavaMailSender javaMailSender;
    @Override
    public void sendEmail(EmailMessage emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(emailMessage.getMessage(),true);
            javaMailSender.send(mimeMessage);
            log.info("이메일 발송 완료 : {}", emailMessage.getMessage());
        }catch(MessagingException e){
            log.error("메일 전송 실패",e);
            throw new RuntimeException(e);
        }

    }
}

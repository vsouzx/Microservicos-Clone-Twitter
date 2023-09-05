package br.com.souza.twitterclone.mailsender.service.impl;

import br.com.souza.twitterclone.mailsender.database.model.Users;
import br.com.souza.twitterclone.mailsender.database.repository.IUsersRepository;
import br.com.souza.twitterclone.mailsender.dto.UserConfirmationCodeResponse;
import br.com.souza.twitterclone.mailsender.service.IMailSenderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailSenderServiceImpl implements IMailSenderService {

    private final static String SUBJECT = "Twitter Clone - Confirmation Code";
    private final static String TEXT = "Your 6 digit confirmation code is: %s";
    private final JavaMailSender mailSender;

    public MailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMailConfirmation(String json) throws Exception {

        UserConfirmationCodeResponse message;
        ObjectMapper mapper = new ObjectMapper();
        message = mapper.readValue(json, UserConfirmationCodeResponse.class);

        var mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getEmail());
        mailMessage.setSubject(SUBJECT);
        mailMessage.setText(String.format(TEXT, message.getConfirmationCode()));

        mailSender.send(mailMessage);
        log.debug("Código de confirmação enviado com SUCESSO!");
    }
}

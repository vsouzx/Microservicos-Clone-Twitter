package br.com.souza.twitterclone.mailsender.service.impl;

import br.com.souza.twitterclone.mailsender.database.model.Users;
import br.com.souza.twitterclone.mailsender.database.repository.IUsersRepository;
import br.com.souza.twitterclone.mailsender.service.IMailSenderService;
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
    private final static String TEXT = "@%s, your 6 digit confirmation code is: %s";
    private final JavaMailSender mailSender;
    private final IUsersRepository iUsersRepository;

    public MailSenderServiceImpl(JavaMailSender mailSender,
                                 IUsersRepository iUsersRepository) {
        this.mailSender = mailSender;
        this.iUsersRepository = iUsersRepository;
    }

    @Override
    public void sendMailConfirmation(String email){
        final String confirmationCode = generateRandomCode();
        Optional<Users> user = iUsersRepository.findByEmail(email);

        if(user.isPresent()){
            user.get().setConfirmationCode(confirmationCode);
            iUsersRepository.save(user.get());

            var message = new SimpleMailMessage();
            message.setTo(user.get().getEmail());
            message.setSubject(SUBJECT);
            message.setText(String.format(TEXT, user.get().getUsername(), confirmationCode));
            mailSender.send(message);
            log.debug("Código de confirmação enviado com SUCESSO!");
        }
    }

    private String generateRandomCode(){
        Random rnd = new Random();
        int randomNumber = rnd.nextInt(999999);
        return String.format("%06d", randomNumber);
    }
}

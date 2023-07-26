package br.com.souza.twitterclone.mailsender.listener;

import br.com.souza.twitterclone.mailsender.service.IMailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableKafka
public class NewRegisterListener {

    private static final String TOPIC = "twitterclone-new-register";
    private static final String GROUP = "twitterclone";
    private final IMailSenderService iMailSenderService;

    public NewRegisterListener(IMailSenderService iMailSenderService) {
        this.iMailSenderService = iMailSenderService;
    }

    @KafkaListener(topics = TOPIC, groupId = GROUP)
    public void receiveEmail(String email) throws Exception {
        log.info("Consumer: e-mail recebido: {} ", email);
        try{
            iMailSenderService.sendMailConfirmation(email);
        }catch (Exception e){
            log.error("Erro ao enviar código");
        }
    }
}

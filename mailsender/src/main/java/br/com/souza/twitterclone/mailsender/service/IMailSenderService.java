package br.com.souza.twitterclone.mailsender.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IMailSenderService {

    void sendMailConfirmation(String email) throws Exception;
}

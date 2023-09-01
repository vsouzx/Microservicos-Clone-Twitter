package br.comsouza.twitterclone.feed.service.posts.impl;

import br.comsouza.twitterclone.feed.client.IAccountsClient;
import br.comsouza.twitterclone.feed.client.IChatGPTClient;
import br.comsouza.twitterclone.feed.database.model.Tweets;
import br.comsouza.twitterclone.feed.database.repository.ITweetsRepository;
import br.comsouza.twitterclone.feed.dto.client.GPTRequest;
import br.comsouza.twitterclone.feed.dto.client.GPTResponse;
import br.comsouza.twitterclone.feed.dto.client.UserDetailsResponse;
import br.comsouza.twitterclone.feed.service.posts.IPostsMessageTranslatorService;
import br.comsouza.twitterclone.feed.util.TextHelper;
import java.math.BigDecimal;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class PostsMessageTranslatorServiceImpl implements IPostsMessageTranslatorService {

    private final IChatGPTClient iChatGPTClient;
    private final IAccountsClient iAccountsClient;
    private final ITweetsRepository iTweetsRepository;
    private final String prompt = "Por favor traduza esta mensagem do %s para o %: %s";

    public PostsMessageTranslatorServiceImpl(IChatGPTClient iChatGPTClient,
                                             IAccountsClient iAccountsClient,
                                             ITweetsRepository iTweetsRepository) {
        this.iChatGPTClient = iChatGPTClient;
        this.iAccountsClient = iAccountsClient;
        this.iTweetsRepository = iTweetsRepository;
    }

    @Override
    public void translateMessage(Tweets tweet, String authorization) throws Exception {

        UserDetailsResponse sessionUser = iAccountsClient.getUserDetails(authorization);
        if (sessionUser == null) {
            throw new Exception("User not found");
        }

        new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                GPTResponse response = iChatGPTClient.generate("Bearer " /*+ apiKey*/,
                        GPTRequest.builder()
                                .prompt(String.format(prompt,
                                        sessionUser.getLanguagePreference().equals("pt") ? "português" : "inglês",
                                        sessionUser.getLanguagePreference().equals("pt") ? "inglês" : "português",
                                        tweet.getMessage())
                                )
                                .max_tokens(1000)
                                .n(1)
                                .temperature(new BigDecimal("0.8"))
                                .stop(null)
                                .build());

                tweet.setMessageTranslations(TextHelper.removeQuotationMarksAndDots(response.getChoices().get(0).getText()));
                iTweetsRepository.save(tweet);
            }
        }.start();
    }
}

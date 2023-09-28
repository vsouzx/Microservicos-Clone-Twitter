package br.com.souza.twitterclone.notifications.configuration.initialdata;

import br.com.souza.twitterclone.notifications.database.model.NotificationsTypes;
import br.com.souza.twitterclone.notifications.database.repository.INotificationsTypesRepository;
import br.com.souza.twitterclone.notifications.enums.NotificationsTypeEnum;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DefaultSettings {

    @Bean
    public CommandLineRunner run(INotificationsTypesRepository iNotificationsTypesRepository) {
        return args -> {
            defaultRgbMapper(iNotificationsTypesRepository);
        };
    }

    private void defaultRgbMapper(INotificationsTypesRepository iNotificationsTypesRepository){
        Optional<NotificationsTypes> newFollowerType = iNotificationsTypesRepository.findByDescription(NotificationsTypeEnum.NEW_FOLLOWER.toString());
        if(newFollowerType.isEmpty()){
            iNotificationsTypesRepository.save(NotificationsTypes.builder()
                            .typeIdentifier(UUID.randomUUID().toString())
                            .description(NotificationsTypeEnum.NEW_FOLLOWER.toString())
                    .build());
        }

        Optional<NotificationsTypes> newCommentType = iNotificationsTypesRepository.findByDescription(NotificationsTypeEnum.NEW_COMMENT.toString());
        if(newCommentType.isEmpty()){
            iNotificationsTypesRepository.save(NotificationsTypes.builder()
                    .typeIdentifier(UUID.randomUUID().toString())
                    .description(NotificationsTypeEnum.NEW_COMMENT.toString())
                    .build());
        }

        Optional<NotificationsTypes> newLikeType = iNotificationsTypesRepository.findByDescription(NotificationsTypeEnum.NEW_LIKE.toString());
        if(newLikeType.isEmpty()){
            iNotificationsTypesRepository.save(NotificationsTypes.builder()
                    .typeIdentifier(UUID.randomUUID().toString())
                    .description(NotificationsTypeEnum.NEW_LIKE.toString())
                    .build());
        }

        Optional<NotificationsTypes> newRetweetType = iNotificationsTypesRepository.findByDescription(NotificationsTypeEnum.NEW_RETWEET.toString());
        if(newRetweetType.isEmpty()){
            iNotificationsTypesRepository.save(NotificationsTypes.builder()
                    .typeIdentifier(UUID.randomUUID().toString())
                    .description(NotificationsTypeEnum.NEW_RETWEET.toString())
                    .build());
        }

        Optional<NotificationsTypes> newPostType = iNotificationsTypesRepository.findByDescription(NotificationsTypeEnum.NEW_POST.toString());
        if(newPostType.isEmpty()){
            iNotificationsTypesRepository.save(NotificationsTypes.builder()
                    .typeIdentifier(UUID.randomUUID().toString())
                    .description(NotificationsTypeEnum.NEW_POST.toString())
                    .build());
        }
    }

}
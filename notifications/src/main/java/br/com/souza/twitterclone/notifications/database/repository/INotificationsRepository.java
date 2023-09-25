package br.com.souza.twitterclone.notifications.database.repository;

import br.com.souza.twitterclone.notifications.database.model.Notifications;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INotificationsRepository extends JpaRepository<Notifications, String> {

    Page<Notifications> findAllByUserReceiverIdentifier(String userIdentifier, Pageable pageable);

    List<Notifications> findByUserSenderIdentifierAndUserReceiverIdentifierAndTypeIdentifierAndTweetIdentifier(String userSenderIdentifier,
                                                                                                               String userReceiverIdentifier,
                                                                                                               String typeIdentifier,
                                                                                                               String tweetIdentifier);
}

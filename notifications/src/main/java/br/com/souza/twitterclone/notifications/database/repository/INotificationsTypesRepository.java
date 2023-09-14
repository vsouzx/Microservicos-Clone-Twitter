package br.com.souza.twitterclone.notifications.database.repository;

import br.com.souza.twitterclone.notifications.database.model.NotificationsTypes;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INotificationsTypesRepository extends JpaRepository<NotificationsTypes, String> {

    Optional<NotificationsTypes> findByDescription(String description);
}

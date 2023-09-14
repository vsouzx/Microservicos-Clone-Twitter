package br.com.souza.twitterclone.notifications.database.repository;

import br.com.souza.twitterclone.notifications.database.model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INotificationsRepository extends JpaRepository<Notifications, String> {
}

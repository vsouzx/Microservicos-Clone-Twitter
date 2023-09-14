package br.com.souza.twitterclone.notifications.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "notifications_types",
        indexes = {
                @Index(name = "UC_notifications_types", columnList = "description", unique = true)
        })
public class NotificationsTypes {

    @Id
    @Column(name = "type_identifier", length = 36)
    private String typeIdentifier;

    @Column(name = "description", length = 50)
    private String description;
}

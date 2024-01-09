package br.comsouza.twitterclone.feed.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "core_config",
        indexes = {
                @Index(name = "UC_core_config", columnList = "key_name", unique = true)
        })
public class CoreConfig implements Serializable {

    @Id
    @Column(name = "identifier", length = 36)
    private String identifier;

    @Column(name = "key_name")
    private String keyName;

    @Column(name = "key_value")
    private String keyValue;
}

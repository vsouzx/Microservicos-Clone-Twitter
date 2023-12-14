package br.com.souza.twitterclone.gateway.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UsefulDate {

    private static final ZoneId ZONE_ID = ZoneId.of("Brazil/East");

    public static LocalDateTime now(){
        return LocalDateTime.now(ZONE_ID);
    }
}

package br.com.souza.twitterclone.accounts.util;

import java.util.Random;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RandomNumberUtil {

    public static String generateRandomCode(){
        Random rnd = new Random();
        int randomNumber = rnd.nextInt(999999);
        return String.format("%06d", randomNumber);
    }
}

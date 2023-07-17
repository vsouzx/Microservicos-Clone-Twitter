package br.com.souza.twitterclone.accounts.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UsernameValidatorHelper {

    private final static String REGEX = "^[a-zA-Z0-9_]*$";

    public static boolean isValidUsername(String username){
        return !username.matches(REGEX);
    }
}

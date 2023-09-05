package br.com.souza.twitterclone.accounts.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PasswordValidatorHelper {

    public static boolean isValidPassword(String password){
        return password.toCharArray().length >= 8 && password.toCharArray().length <= 15;
    }
}

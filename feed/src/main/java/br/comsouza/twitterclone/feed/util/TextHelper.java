package br.comsouza.twitterclone.feed.util;

import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor
public class TextHelper {

    public static String removeQuotationMarksAndDots(String text){
        return text.replaceAll("[\".]", "");
    }
}

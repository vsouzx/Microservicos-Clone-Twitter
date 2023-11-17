package br.comsouza.twitterclone.feed.util;

import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor
public class TextHelper {

    public static String removeQuotationMarksAndDots(String text){
        return text.replaceAll("[\".]", "");
    }

    public static String extractExtension(String file, String name){
        return name + file.substring(file.lastIndexOf("."));
    }
}

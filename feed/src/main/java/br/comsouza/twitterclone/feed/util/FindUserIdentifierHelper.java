package br.comsouza.twitterclone.feed.util;

import br.comsouza.twitterclone.feed.handler.exceptions.ApiAuthorizationException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@NoArgsConstructor
public class FindUserIdentifierHelper {

    public static String getIdentifier() throws Exception {
        RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(attribs)).getRequest();
        String userIdentifier = request.getHeader("LOGGED_USER_IDENTIFIER");
        if (userIdentifier == null) {
            throw new ApiAuthorizationException();
        }
        return userIdentifier;
    }
}

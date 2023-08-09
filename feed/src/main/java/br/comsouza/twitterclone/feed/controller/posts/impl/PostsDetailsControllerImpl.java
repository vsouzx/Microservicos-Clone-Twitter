package br.comsouza.twitterclone.feed.controller.posts.impl;

import br.comsouza.twitterclone.feed.controller.posts.IPostsDetailsController;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.posts.IPostsDetailsService;
import br.comsouza.twitterclone.feed.util.FindUserIdentifierHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/posts/detail")
public class PostsDetailsControllerImpl implements IPostsDetailsController {

    private final IPostsDetailsService iPostsDetailsService;

    public PostsDetailsControllerImpl(IPostsDetailsService iPostsDetailsService) {
        this.iPostsDetailsService = iPostsDetailsService;
    }

    //TODO: Detalhar um tweet (lista comentarios paginado, qtd reweets sem valor, qtd tweets com valor, qtd curtidas, qtd itens salvos, qtd visualizacoes, horario e data post)
    @GetMapping(value = "/{tweetIdentifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimelineTweetResponse> getTweetDetails(@PathVariable("tweetIdentifier") String tweetIdentifier) throws Exception {
        return new ResponseEntity<>(iPostsDetailsService.getTweetDetails(FindUserIdentifierHelper.getIdentifier(), tweetIdentifier), HttpStatus.OK);
    }

    //TODO: Detalhar lista de retweets sem valor de um tweet

    //TODO: Detalhar lista de retweets com valor de um tweet

    //TODO: Detalhar lista de curtidas de um tweet

}

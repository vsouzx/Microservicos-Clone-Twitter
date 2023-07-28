package br.comsouza.twitterclone.feed.service.tweettype.impl;

import br.comsouza.twitterclone.feed.database.model.TweetsTypes;
import br.comsouza.twitterclone.feed.database.repository.ITweetsTypesRepository;
import br.comsouza.twitterclone.feed.service.tweettype.ITweetTypeService;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TweetTypeServiceImpl implements ITweetTypeService {

    private final ITweetsTypesRepository iTweetsTypesRepository;

    public TweetTypeServiceImpl(ITweetsTypesRepository iTweetsTypesRepository) {
        this.iTweetsTypesRepository = iTweetsTypesRepository;
    }

    @Override
    public TweetsTypes findTweetTypeByDescription(String description) {

        TweetsTypes type = iTweetsTypesRepository.findByDescription(description).orElse(null);

        if (type == null) {
            type = iTweetsTypesRepository.save(TweetsTypes.builder()
                    .typeIdentifier(UUID.randomUUID().toString())
                    .description(description)
                    .build());
        }

        return type;
    }

}

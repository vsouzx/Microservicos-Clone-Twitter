package br.comsouza.twitterclone.feed.controller.timeline.impl;

import br.comsouza.twitterclone.feed.controller.timeline.ITimelineController;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import br.comsouza.twitterclone.feed.service.redis.RedisService;
import br.comsouza.twitterclone.feed.service.timeline.ITimelineService;
import br.comsouza.twitterclone.feed.util.FindUserIdentifierHelper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/timeline")
public class TimelineControllerImpl implements ITimelineController {

    private final ITimelineService iTimelineService;
    private final RedisService redisService;

    public TimelineControllerImpl(ITimelineService iTimelineService,
                                  RedisService redisService) {
        this.iTimelineService = iTimelineService;
        this.redisService = redisService;
    }

    @GetMapping(value = "/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TimelineTweetResponse>> getFollowingTimeline(@PathVariable(value = "type", required = true) String type,
                                                                            @RequestParam(value = "targetUserIdentifier", required = false) String targetUserIdentifier,
                                                                            @RequestParam(value = "page", required = true) Integer page,
                                                                            @RequestParam(value = "size", required = true) Integer size) throws Exception{
        String sessionUserIdentifier = FindUserIdentifierHelper.getIdentifier();
        redisService.isValidUser(sessionUserIdentifier);
        return new ResponseEntity<>(iTimelineService.getTimeline(sessionUserIdentifier, page, size, type, targetUserIdentifier), HttpStatus.OK);
    }

}

package br.com.souza.twitterclone.directmessages.controller.impl;

import br.com.souza.twitterclone.directmessages.controller.IDirectMessageController;
import br.com.souza.twitterclone.directmessages.dto.ChatsMessageResponse;
import br.com.souza.twitterclone.directmessages.dto.ChatsResponse;
import br.com.souza.twitterclone.directmessages.service.directmessages.IDirectMessagesService;
import br.com.souza.twitterclone.directmessages.util.FindUserIdentifierHelper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/chat")
public class DirectMessagesControllerImpl implements  IDirectMessageController {

    private final IDirectMessagesService iDirectMessagesService;

    public DirectMessagesControllerImpl(IDirectMessagesService iDirectMessagesService) {
        this.iDirectMessagesService = iDirectMessagesService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChatsResponse>> getAllChats() throws Exception {
        return new ResponseEntity<>(iDirectMessagesService.getAllChats(FindUserIdentifierHelper.getIdentifier()), HttpStatus.OK);
    }

    @GetMapping(value = "/{chatIdentifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChatsMessageResponse>> getSpecificChat(@PathVariable("chatIdentifier") String chatIdentifier,
                                                                      @RequestHeader("Authorization") String authorization)  throws Exception {
        return new ResponseEntity<>(iDirectMessagesService.getSpecificChat(FindUserIdentifierHelper.getIdentifier(), chatIdentifier, authorization), HttpStatus.OK);
    }

//    @GetMapping(value = "/new", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<ChatsResponse>> newChat(){
//        return new ResponseEntity<>(iDirectMessagesService.getAllChats(FindUserIdentifierHelper.getIdentifier()), HttpStatus.OK);
//    }
}

package br.com.souza.twitterclone.accounts.service.interactions.impl;

import br.com.souza.twitterclone.accounts.database.model.BlockedUsers;
import br.com.souza.twitterclone.accounts.database.model.BlockedUsersId;
import br.com.souza.twitterclone.accounts.database.model.User;
import br.com.souza.twitterclone.accounts.database.repository.BlockedUsersRepository;
import br.com.souza.twitterclone.accounts.database.repository.UserRepository;
import br.com.souza.twitterclone.accounts.handler.exceptions.UserNotFoundException;
import br.com.souza.twitterclone.accounts.service.interactions.IUsersInteractionsService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsersInteractionsServiceImpl implements IUsersInteractionsService {

    private final UserRepository userRepository;
    private final BlockedUsersRepository blockedUsersRepository;

    public UsersInteractionsServiceImpl(UserRepository userRepository,
                                        BlockedUsersRepository blockedUsersRepository) {
        this.userRepository = userRepository;
        this.blockedUsersRepository = blockedUsersRepository;
    }

    public void blockToggle(String sessionUserIdentifier, String targetUserIdentifier) throws Exception {
        Optional<User> user = userRepository.findById(targetUserIdentifier);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        Optional<BlockedUsers> targetUserIsBlocked = blockedUsersRepository.findById(BlockedUsersId.builder()
                .blockerIdentifier(sessionUserIdentifier)
                .blockedIdentifier(targetUserIdentifier)
                .build());

        if (targetUserIsBlocked.isPresent()) {
            blockedUsersRepository.delete(targetUserIsBlocked.get());
        } else {
            blockedUsersRepository.save(BlockedUsers.builder()
                    .id(BlockedUsersId.builder()
                            .blockerIdentifier(sessionUserIdentifier)
                            .blockedIdentifier(targetUserIdentifier)
                            .build())
                    .build());
        }
    }
}

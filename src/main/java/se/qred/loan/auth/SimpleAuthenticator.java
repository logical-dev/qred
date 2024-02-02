package se.qred.loan.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import se.qred.loan.model.User;
import se.qred.loan.db.UserRepository;
import java.util.Optional;

public class SimpleAuthenticator implements Authenticator<BasicCredentials, User> {

    private UserRepository userRepository;

    public SimpleAuthenticator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        User user = userRepository.findByUsername(credentials.getUsername());

        if (user == null) {
            return Optional.empty();
        }

        if (user.getPassword().equals(credentials.getPassword())) {
            return Optional.of(user);
        }

        return Optional.empty();
    }
}
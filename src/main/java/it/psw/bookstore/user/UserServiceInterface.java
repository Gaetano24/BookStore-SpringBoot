package it.psw.bookstore.user;

import it.psw.bookstore.support.RegistrationRequest;
import it.psw.bookstore.support.exceptions.UserNotFoundException;

import java.util.List;

public interface UserServiceInterface {
    List<User> findAll();
    User findByEmail(String email) throws UserNotFoundException;
    User register(RegistrationRequest registrationRequest) throws Exception;

}
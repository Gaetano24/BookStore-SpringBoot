package it.psw.bookstore.user;

import it.psw.bookstore.exceptions.MailUserAlreadyExistsException;
import it.psw.bookstore.exceptions.UserNotFoundException;

import java.util.List;

public interface UserServiceInterface {
    List<User> findAll();
    User findByEmail(String email) throws UserNotFoundException;
    User save(User user) throws MailUserAlreadyExistsException;

}

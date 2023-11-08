package it.psw.bookstore.support;

import it.psw.bookstore.user.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class RegistrationRequest implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String password;

    public User getUser() {
        User user = new User();
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setEmail(this.email);
        user.setAddress(this.address);
        user.setPhone(this.phone);
        return user;
    }

}

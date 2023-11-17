package it.psw.bookstore.support;

import it.psw.bookstore.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class RegistrationRequest implements Serializable {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String address;

    @NotBlank
    private String phone;

    @NotBlank
    @Size(min = 8)
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

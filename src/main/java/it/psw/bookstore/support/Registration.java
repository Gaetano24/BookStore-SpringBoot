package it.psw.bookstore.support;


import it.psw.bookstore.support.exceptions.KeycloackRegistrationException;
import it.psw.bookstore.user.User;
import lombok.experimental.UtilityClass;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class Registration {
    public void keycloackRegistration (RegistrationRequest registrationRequest) throws KeycloackRegistrationException {
        try {
            String usernameAdmin = "marchianogaetano@gmail.com";
            String passwordAdmin = "password";
            String clientName = "BookStore-API";
            String role = "user";
            String serverUrl = "http://localhost:8080";
            String realm = "BookStore";

            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(clientName)
                    .username(usernameAdmin)
                    .password(passwordAdmin)
                    .build();

            User utente = registrationRequest.getUser();
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(utente.getEmail());
            user.setEmail(utente.getEmail());
            user.setFirstName(utente.getFirstName());
            user.setLastName(utente.getLastName());
            user.setAttributes(Collections.singletonMap("origin", List.of("demo")));

            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            Response response = usersResource.create(user);
            System.out.printf("Response: %s %s%n", response.getStatus(), response.getStatusInfo());
            System.out.println(response.getLocation());
            String userId = CreatedResponseUtil.getCreatedId(response);
            System.out.printf("User created with userId: %s%n", userId);

            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(registrationRequest.getPassword());
            UserResource userResource = usersResource.get(userId);
            userResource.resetPassword(passwordCred);

            RoleRepresentation testerRealmRole = realmResource.roles().get(role).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(testerRealmRole));

        } catch (Exception e) {
            throw new KeycloackRegistrationException();
        }
    }
}
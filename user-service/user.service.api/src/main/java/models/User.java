package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    private Long id;

    private String username;

    private String password;

    private String role;

    private String firstName;

    private String lastName;

    private String email;

    private String details;

}

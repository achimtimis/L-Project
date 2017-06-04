package app.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by achy_ on 6/4/2017.
 */

@Entity(name = "user_profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "firstName")
    private String fistName;

    @Column(name = "lastName")
    private String lastname;

    @Column(name = "details")
    private String details;


}

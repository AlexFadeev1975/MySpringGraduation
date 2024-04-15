package org.example.data.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.data.model.enums.RoleType;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String userName;

    String password;

    String email;

    @Enumerated(EnumType.STRING)
    RoleType role;

    public String toString() {

        return " userName " + userName + " role " + role.toString();
    }
}

package org.max.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contacts")
public class Contacts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // TODO add validation
    @Column(name = "phone")
    @NotBlank(message = "Phone is required")
    private String phone;

    @Column(name = "email")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid and should look like 'email@example.com'")
    private String email;
}

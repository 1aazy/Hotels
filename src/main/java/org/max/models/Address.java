package org.max.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "house_number")
    @Min(value = 1, message = "House number must be greater than 0")
    private int houseNumber;

    @Column(name = "street")
    @NotBlank(message = "Street is required")
    private String street;

    @Column(name = "city")
    @NotBlank(message = "City is required")
    private String city;

    @Column(name = "country")
    @NotBlank(message = "Country is required")
    private String country;

    @Column(name = "post_code")
    @NotBlank(message = "Post code is required")
    private String postCode;
}

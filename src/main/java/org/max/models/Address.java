package org.max.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.metamodel.model.domain.IdentifiableDomainType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
// TODO add validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "house_number")
    private int houseNumber;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "post_code")
    private String postCode;
}

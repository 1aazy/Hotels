package org.max.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    @NotBlank(message = "Hotel name is required")
    private String name;

    @Column(name = "brand")
    @NotBlank(message = "Hotel brand is required")
    private String brand;

    @Column(name = "description")
    private String description;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    @Valid
    private Address address;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "contacts_id")
    @Valid
    private Contacts contacts;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "arrival_time_id")
    @Valid
    private ArrivalTime arrivalTime;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "hotels_amenities",
               joinColumns = @JoinColumn(name = "hotel_id"),
               inverseJoinColumns = @JoinColumn(name = "amenity_id"))
    @Valid
    private Set<Amenity> amenities;
}
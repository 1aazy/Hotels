package org.max.models;

import jakarta.persistence.*;
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

    private String name;

    private String brand;

    private String description;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "contacts_id")
    private Contacts contacts;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "arrival_time_id")
    private ArrivalTime arrivalTime;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "hotels_amenities",
               joinColumns = @JoinColumn(name = "hotel_id"),
               inverseJoinColumns = @JoinColumn(name = "aminities_id"))
    private Set<Amenity> amenities;
}

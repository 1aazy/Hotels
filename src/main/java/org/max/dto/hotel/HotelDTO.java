package org.max.dto.hotel;

import lombok.*;
import org.max.dto.AddressDTO;
import org.max.dto.ArrivalTimeDTO;
import org.max.dto.ContactsDTO;
import org.max.models.Amenity;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelDTO {

    Long id;

    String name;

    String brand;

    AddressDTO address;

    ContactsDTO contacts;

    ArrivalTimeDTO arrivalTime;

    Set<Amenity> amenities;
}
